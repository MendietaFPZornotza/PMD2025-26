package com.zerbitzaria.tcp.handler.auth;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler implements TcpHandler {

    private final ErabiltzaileaService service;
    private final ErabiltzaileaRepository repo;

    public LoginHandler(ErabiltzaileaService service, ErabiltzaileaRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @Override
    public String command() {
        return "LOGIN";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 2) return "ERROR;BAD_ARGS";

        String email = req.args()[0].trim();
        String pass  = req.args()[1];

        boolean ok = service.login(email, pass);
        if (!ok) return "LOGIN_FAIL";

        Erabiltzailea user = repo.findByEmaila(email).orElse(null);
        if (user == null) return "LOGIN_FAIL";

        return "LOGIN_OK;"
                + nz(user.getId()) + ";"
                + safe(user.getEmaila()) + ";"
                + safe(user.getIzena());

    }

    private String safe(String s) {
        return s == null ? "" : s.replace(";", " ").replace("|", " ");
    }

    private String nz(Object o) {
        return o == null ? "0" : o.toString();
    }
}