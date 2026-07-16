package com.zerbitzaria.sarrera.service;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.mail.EmailService;
import com.zerbitzaria.mail.FTPService;
import com.zerbitzaria.mail.PurchasePostProcessor;
import com.zerbitzaria.mail.TicketPdfService;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.common.enums.SarreraEgoera;
import com.zerbitzaria.ekitaldia.repository.EkitaldiaRepository;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ErositakoSarreraService {

    private final ErositakoSarreraRepository sarreraRepo;
    private final ErabiltzaileaRepository erabiltzaileaRepo;
    private final EkitaldiaRepository ekitaldiaRepo;
    private final PurchasePostProcessor post;

    private final TicketPdfService pdfService;
    private final FTPService ftpService;
    private final EmailService emailService;

    public ErositakoSarreraService(
            ErositakoSarreraRepository sarreraRepo,
            ErabiltzaileaRepository erabiltzaileaRepo,
            EkitaldiaRepository ekitaldiaRepo, PurchasePostProcessor post,
            TicketPdfService pdfService,
            FTPService ftpService,
            EmailService emailService
    ) {
        this.sarreraRepo = sarreraRepo;
        this.erabiltzaileaRepo = erabiltzaileaRepo;
        this.ekitaldiaRepo = ekitaldiaRepo;
        this.post = post;
        this.pdfService = pdfService;
        this.ftpService = ftpService;
        this.emailService = emailService;
    }

    // Método “público”: BD rápido + post-proceso fuera
    public String erosiMulti(int erabiltzaileaId, int ekitaldiaId, List<String> seatCodes, String sendEmail) {

        // 1) BD: transacción corta (esto debe tardar milisegundos)
        String deskargaKodea = erosiMultiDbTx(erabiltzaileaId, ekitaldiaId, seatCodes);
        post.run(deskargaKodea, sendEmail);
        // 2) Post-proceso async: NO bloquea la respuesta TCP
        postProcessPurchaseAsync(deskargaKodea, sendEmail);


        // 3) RESPONDER YA al móvil
        return deskargaKodea;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected String erosiMultiDbTx(int erabiltzaileaId, int ekitaldiaId, List<String> seatCodes) {

        Erabiltzailea erabiltzailea = erabiltzaileaRepo.findById((long) erabiltzaileaId)
                .orElseThrow(() -> new RuntimeException("Erabiltzailea ez da existitzen: id=" + erabiltzaileaId));

        Ekitaldia ekitaldia = ekitaldiaRepo.findById(ekitaldiaId)
                .orElseThrow(() -> new RuntimeException("Ekitaldia ez da existitzen: id=" + ekitaldiaId));

        if (seatCodes == null || seatCodes.isEmpty()) {
            throw new RuntimeException("Eserlekuak hutsik");
        }

        String deskargaKodea = generateShortCode(8);

        List<ErositakoSarrera> batch = new ArrayList<>();

        for (String seat : seatCodes) {
            SeatPos pos = parseSeat(seat);

            ErositakoSarrera s = new ErositakoSarrera();
            s.setDeskarga_kodea(deskargaKodea);
            s.setKodea(UUID.randomUUID().toString());
            s.setFila(pos.fila());
            s.setEserlekua(pos.eserlekua());
            s.setErabiltzailea(erabiltzailea);
            s.setEkitaldia(ekitaldia);
            s.setErosketarenData(LocalDateTime.now());
            s.setEgoera(SarreraEgoera.BALIOZKOA);

            batch.add(s);
        }

        try {
            sarreraRepo.saveAll(batch);
            sarreraRepo.flush(); // para detectar duplicados aquí
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("Eserlekua okupatuta");
        }

        return deskargaKodea;
    }

    @Async
    protected void postProcessPurchaseAsync(String deskargaKodea, String sendEmail) {
        try {
            // 1) cargar tickets con join fetch
            List<ErositakoSarrera> tickets = sarreraRepo.findByDeskargaKodeaWithEvent(deskargaKodea);
            if (tickets.isEmpty()) return;

            // 2) generar pdf (1 por compra)
            File pdf = pdfService.generatePdf(deskargaKodea, tickets);

            // 3) subir al ftp
            String remoteName = deskargaKodea + ".pdf";
            String remotePath = ftpService.uploadFile(pdf, remoteName);

            // 4) guardar path en todos
            for (ErositakoSarrera s : tickets) {
                s.setQrPath(remotePath);
            }
            sarreraRepo.saveAll(tickets);

            // 5) email (si falla, NO rompe nada)
            emailService.sendDownloadCode(sendEmail, deskargaKodea);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private record SeatPos(int fila, int eserlekua) {}

    private SeatPos parseSeat(String code) {
        if (code == null) throw new RuntimeException("Seat null");
        String c = code.trim().toUpperCase();

        // formato: A1, B10, AA3...
        int i = 0;
        while (i < c.length() && Character.isLetter(c.charAt(i))) i++;
        if (i == 0 || i == c.length()) throw new RuntimeException("Seat format bad: " + code);

        String rowStr = c.substring(0, i);
        String colStr = c.substring(i);

        int fila = rowToNumber(rowStr);
        int eser = Integer.parseInt(colStr);

        if (fila <= 0 || eser <= 0) throw new RuntimeException("Seat invalid: " + code);
        return new SeatPos(fila, eser);
    }

    private int rowToNumber(String row) {
        // A=1, B=2 ... Z=26, AA=27...
        int n = 0;
        for (int k = 0; k < row.length(); k++) {
            char ch = row.charAt(k);
            if (ch < 'A' || ch > 'Z') throw new RuntimeException("Row invalid: " + row);
            n = n * 26 + (ch - 'A' + 1);
        }
        return n;
    }

    private static final char[] ALPH = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray(); // sin 0/O/1/I
    private final SecureRandom rnd = new SecureRandom();

    private String generateShortCode(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int k = 0; k < len; k++) sb.append(ALPH[rnd.nextInt(ALPH.length)]);
        return sb.toString();
    }

    public List<ErositakoSarrera> findAll() {
        return sarreraRepo.findAll();
    }

    public ErositakoSarrera findById(int id) {
        return sarreraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sarrera ez da existitzen: id=" + id));
    }

    public ErositakoSarrera findByKodea(String kodea) {
        return sarreraRepo.findByKodea(kodea)
                .orElseThrow(() -> new RuntimeException("Kodea ez da existitzen: " + kodea));
    }

    public List<ErositakoSarrera> findByErabiltzailea(int erabiltzaileaId) {
        return sarreraRepo.findByErabiltzailea_Id(erabiltzaileaId);
    }

    public ErositakoSarrera erosi(long erabiltzaileaId, int ekitaldiaId) {
        Erabiltzailea erabiltzailea = erabiltzaileaRepo.findById(erabiltzaileaId)
                .orElseThrow(() -> new RuntimeException("Erabiltzailea ez da existitzen: id=" + erabiltzaileaId));

        Ekitaldia ekitaldia = ekitaldiaRepo.findById(ekitaldiaId)
                .orElseThrow(() -> new RuntimeException("Ekitaldia ez da existitzen: id=" + ekitaldiaId));

        ErositakoSarrera sarrera = new ErositakoSarrera();

        // Kodea unikua
        String kodea = UUID.randomUUID().toString();
        sarrera.setKodea(kodea);

        sarrera.setErabiltzailea(erabiltzailea);
        sarrera.setEkitaldia(ekitaldia);

        // Erosketa data
        sarrera.setErosketarenData(LocalDateTime.now());

        // Egoera hasierakoa
        sarrera.setEgoera(SarreraEgoera.BALIOZKOA);

        return sarreraRepo.save(sarrera);
    }

    public ErositakoSarrera markatuErabilita(String kodea) {
        ErositakoSarrera sarrera = findByKodea(kodea);

        if (sarrera.getEgoera() == SarreraEgoera.ERABILITA) {
            throw new RuntimeException("Sarrera jada erabilita dago");
        }
        if (sarrera.getEgoera() == SarreraEgoera.EZEZTATUA) {
            throw new RuntimeException("Sarrera ezeztatuta dago");
        }

        sarrera.setEgoera(SarreraEgoera.ERABILITA);
        sarrera.setErabilitaNoiz(LocalDateTime.now());

        return sarreraRepo.save(sarrera);
    }

    public void ezeztatu(String kodea) {
        ErositakoSarrera sarrera = findByKodea(kodea);

        if (sarrera.getEgoera() == SarreraEgoera.ERABILITA) {
            throw new RuntimeException("Ezin da ezeztatu: sarrera erabilita dago");
        }

        sarrera.setEgoera(SarreraEgoera.EZEZTATUA);
        sarreraRepo.save(sarrera);
    }

    /**
     * Ekitaldi bateko sarrera erosien kopurua bueltatzen du.
     */
    public long countByEkitaldia(int ekitaldiaId) {
        return sarreraRepo.countByEkitaldia_Id(ekitaldiaId);
    }

    public void delete(int id) {
        if (!sarreraRepo.existsById(id)) {
            throw new RuntimeException("Ezin da ezabatu, ez da existitzen: id=" + id);
        }
        sarreraRepo.deleteById(id);
    }
}