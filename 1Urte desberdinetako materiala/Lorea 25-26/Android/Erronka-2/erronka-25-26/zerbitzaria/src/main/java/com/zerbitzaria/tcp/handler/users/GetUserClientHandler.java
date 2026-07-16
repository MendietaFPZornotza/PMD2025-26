package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

/**
 * PROFILE_GET;email
 * Respuestas:
 *  PROFILE_OK;izena;email
 *  PROFILE_NOT_FOUND
 *  PROFILE_FAIL
 */
@Component
public class GetUserClientHandler implements TcpHandler {

    private final ErabiltzaileaRepository repo;

    public GetUserClientHandler(ErabiltzaileaRepository repo) {
        this.repo = repo;
    }

    @Override public String command() { return "PROFILE_GET"; }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 1) return "ERROR;BAD_ARGS";

        String email = safeIn(req.args()[0]).trim().toLowerCase();
        return repo.findByEmaila(email)
                .map(u -> "PROFILE_OK;" + safeOut(u.getIzena()) + ";" + safeOut(u.getEmaila()))
                .orElse("PROFILE_NOT_FOUND");
    }

    private String safeIn(String s) { return s == null ? "" : s; }
    private String safeOut(String s) { return s == null ? "" : s.replace(";", " "); }
}