package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteProfileHandler implements TcpHandler {

    private final ErabiltzaileaService service;

    public DeleteProfileHandler(ErabiltzaileaService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "DELETE_PROFILE";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 1) return "DELETE_PROFILE_FAIL;BAD_ARGS";

        int userId;
        try {
            userId = Integer.parseInt(req.args()[0].trim());
        } catch (Exception e) {
            return "DELETE_PROFILE_FAIL;BAD_ID";
        }

        try {
            service.deactivateUser(userId);
            return "DELETE_PROFILE_OK";
        } catch (Exception e) {
            return "DELETE_PROFILE_FAIL;" + safe(e.getMessage());
        }
    }

    private String safe(String s) {
        if (s == null) return "SERVER";
        return s.replace(";", " ").replace("|", " ");
    }
}