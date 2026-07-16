package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserHandler implements TcpHandler {

    private final ErabiltzaileaService service;

    public DeleteUserHandler(ErabiltzaileaService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "DELETE_USER";
    }

    @Override
    public String handle(TcpRequest req) {
        // DELETE_ERABILTZAILEA;<id>
        if (req.args() == null || req.args().length != 1) {
            return "ERROR:BAD_ARGS";
        }

        Long id;
        try {
            id = Long.parseLong(req.args()[0].trim());
        } catch (NumberFormatException e) {
            return "ERROR:ID_BAD_FORMAT";
        }

        // Existitzen den egiaztatu
        if (service.findById(id).isEmpty()) {
            return "ERROR:NOT_FOUND";
        }

        try {
            service.delete(id);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:DELETE_FAIL";
        }
    }
}