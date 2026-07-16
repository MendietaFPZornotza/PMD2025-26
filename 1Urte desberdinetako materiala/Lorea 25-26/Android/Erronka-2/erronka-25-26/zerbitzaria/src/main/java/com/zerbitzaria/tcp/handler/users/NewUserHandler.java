package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.common.enums.ErabiltzaileMota;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class NewUserHandler implements TcpHandler {

    private final ErabiltzaileaRepository repo;
    private final ErabiltzaileaService service;

    public NewUserHandler(ErabiltzaileaRepository repo, ErabiltzaileaService service) {
        this.repo = repo;
        this.service = service;
    }

    @Override
    public String command() {
        return "NEW_USER";
    }

    @Override
    public String handle(TcpRequest req) {
        // NEW_ERABILTZAILEA;izena;emaila;pass;mota
        if (req.args() == null || req.args().length != 4) {
            return "ERROR:BAD_ARGS";
        }

        String izena = safeIn(req.args()[0]).trim();
        String email = safeIn(req.args()[1]).trim().toLowerCase();
        String pass  = safeIn(req.args()[2]);
        String motaRaw = safeIn(req.args()[3]);

        if (email.isBlank() || pass.isBlank()) {
            return "ERROR:EMAIL_OR_PASS_EMPTY";
        }

        if (repo.findByEmaila(email).isPresent()) {
            return "ERROR:EMAIL_EXISTS";
        }

        ErabiltzaileMota mota;
        try {
            // Hutsa bada, BEZEROA bezala utzi (entity-ak ere default dauka)
            mota = parseMotaOrNull(motaRaw);
            if (mota == null) mota = ErabiltzaileMota.BEZEROA;
        } catch (IllegalArgumentException e) {
            return "ERROR:MOTA_OKERRA";
        }

        Erabiltzailea u = new Erabiltzailea();
        u.setIzena(izena);
        u.setEmaila(email);
        u.setPasahitzaHash(pass); // service.create() -> hash egingo du
        u.setMota(mota);

        service.create(u);

        return "OK";
    }

    /**
     * String -> ErabiltzaileMota.
     * - "" bada: null
     * - okerra bada: IllegalArgumentException
     */
    private ErabiltzaileMota parseMotaOrNull(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return ErabiltzaileMota.valueOf(raw.trim().toUpperCase());
    }

    private String safeIn(String s) { return s == null ? "" : s; }
}