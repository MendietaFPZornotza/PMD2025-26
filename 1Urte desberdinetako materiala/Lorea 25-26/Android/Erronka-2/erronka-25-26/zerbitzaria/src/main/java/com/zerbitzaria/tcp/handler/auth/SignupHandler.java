package com.zerbitzaria.tcp.handler.auth;

import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 * Handler del comando SIGNUP (crear usuario).
 * Protocolo:
 *   SIGNUP;izena;email;password
 * Respuesta:
 *   SIGNUP_OK;id;izena;email
 *   SIGNUP_EXISTS
 *   SIGNUP_FAIL;...
 */
@Component
public class SignupHandler implements TcpHandler {

    private final ErabiltzaileaService service;
    private final ErabiltzaileaRepository repo;

    public SignupHandler(ErabiltzaileaService service, ErabiltzaileaRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @Override
    public String command() {
        return "SIGNUP";
    }

    @Override
    public String handle(TcpRequest req) {
        // SIGNUP;izena;email;pass  -> args = 3
        if (req.args().length != 3) return "SIGNUP_FAIL;BAD_ARGS";

        String izena = safeIn(req.args()[0]);
        String email = safeIn(req.args()[1]).toLowerCase();
        String pass  = safeIn(req.args()[2]);

        if (izena.isBlank() || email.isBlank() || pass.isBlank()) return "SIGNUP_FAIL;EMPTY";

        // Fast check
        if (repo.findByEmaila(email).isPresent()) return "SIGNUP_EXISTS";

        try {
            Erabiltzailea u = new Erabiltzailea();
            u.setIzena(izena);
            u.setEmaila(email);
            u.setPasahitzaHash(pass); // el service lo hashea

            Erabiltzailea saved = service.create(u);

            // ✅ Ahora devolvemos también el ID
            return "SIGNUP_OK;"
                    + nz(saved.getId()) + ";"
                    + safeOut(saved.getIzena()) + ";"
                    + safeOut(saved.getEmaila());

        } catch (DataIntegrityViolationException dive) {
            dive.printStackTrace();

            String msg = (dive.getMostSpecificCause() != null)
                    ? dive.getMostSpecificCause().getMessage()
                    : dive.getMessage();

            msg = msg == null ? "DB_CONSTRAINT" : msg.replace(";", " ");
            msg = msg.substring(0, Math.min(msg.length(), 120));

            return "SIGNUP_FAIL;DB_CONSTRAINT;" + msg;

        } catch (Exception e) {
            e.printStackTrace();
            return "SIGNUP_FAIL;SERVER";
        }
    }

    private String safeIn(String s) {
        return s == null ? "" : s.replace(";", " ").trim();
    }

    private String safeOut(String s) {
        return s == null ? "" : s.replace(";", " ").replace("|", " ");
    }

    private String nz(Object o) {
        return o == null ? "0" : o.toString();
    }
}