package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.common.enums.ErabiltzaileMota;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserAdminHandler implements TcpHandler {

    private final ErabiltzaileaRepository repo;
    private final ErabiltzaileaService service;

    public UpdateUserAdminHandler(ErabiltzaileaRepository repo, ErabiltzaileaService service) {
        this.repo = repo;
        this.service = service;
    }

    @Override
    public String command() {
        return "UPDATE_USER";
    }

    @Override
    public String handle(TcpRequest req) {
        // UPDATE_ERABILTZAILEA;id;izenaOrBlank;emailaOrBlank;passOrBlank;motaOrBlank
        if (req.args() == null || req.args().length != 5) {
            return "ERROR:BAD_ARGS";
        }

        Long id;
        try {
            id = Long.parseLong(req.args()[0].trim());
        } catch (NumberFormatException e) {
            return "ERROR:ID_OKERRA";
        }

        Erabiltzailea u = service.findById(id).orElse(null);
        if (u == null) return "ERROR:NOT_FOUND";

        String izena   = safeIn(req.args()[1]).trim();
        String email   = safeIn(req.args()[2]).trim().toLowerCase();
        String pass    = safeIn(req.args()[3]);
        String motaRaw = safeIn(req.args()[4]);

        // Email aldaketa: beste erabiltzaile batek ez daukala egiaztatu
        if (!email.isBlank() && !email.equalsIgnoreCase(u.getEmaila())) {
            if (repo.findByEmaila(email).isPresent()) {
                return "ERROR:EMAIL_EXISTS";
            }
            u.setEmaila(email);
        }

        if (!izena.isBlank()) u.setIzena(izena);
        if (!pass.isBlank()) u.setPasahitzaHash(pass); // service.create() -> hash egingo du

        if (!motaRaw.isBlank()) {
            try {
                ErabiltzaileMota mota = parseMotaOrNull(motaRaw);
                if (mota != null) u.setMota(mota);
            } catch (IllegalArgumentException e) {
                return "ERROR:MOTA_OKERRA";
            }
        }

        // create() erabilita: save + hash logika mantentzen da
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