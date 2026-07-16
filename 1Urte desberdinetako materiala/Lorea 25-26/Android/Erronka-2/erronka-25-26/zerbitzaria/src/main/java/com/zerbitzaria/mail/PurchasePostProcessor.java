package com.zerbitzaria.mail;

import com.zerbitzaria.mail.EmailService;
import com.zerbitzaria.mail.FTPService;
import com.zerbitzaria.mail.TicketPdfService;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class PurchasePostProcessor {

    private final ErositakoSarreraRepository sarreraRepo;
    private final TicketPdfService pdfService;
    private final FTPService ftpService;
    private final EmailService emailService;

    public PurchasePostProcessor(
            ErositakoSarreraRepository sarreraRepo,
            TicketPdfService pdfService,
            FTPService ftpService,
            EmailService emailService
    ) {
        this.sarreraRepo = sarreraRepo;
        this.pdfService = pdfService;
        this.ftpService = ftpService;
        this.emailService = emailService;
    }

    @Async
    public void run(String deskargaKodea, String sendEmail) {
        try {
            List<ErositakoSarrera> tickets = sarreraRepo.findByDeskargaKodeaWithEvent(deskargaKodea);
            if (tickets.isEmpty()) return;

            File pdf = pdfService.generatePdf(deskargaKodea, tickets);

            String remoteName = deskargaKodea + ".pdf";
            String remotePath = ftpService.uploadFile(pdf, remoteName);

            for (ErositakoSarrera s : tickets) s.setQrPath(remotePath);
            sarreraRepo.saveAll(tickets);

            emailService.sendDownloadCode(sendEmail, deskargaKodea);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}