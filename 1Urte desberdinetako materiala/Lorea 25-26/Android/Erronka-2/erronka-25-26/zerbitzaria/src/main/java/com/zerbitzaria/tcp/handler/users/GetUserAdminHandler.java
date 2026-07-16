package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class GetUserAdminHandler implements TcpHandler {

    private final ErabiltzaileaService service;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public GetUserAdminHandler(ErabiltzaileaService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "GET_USER";
    }

    @Override
    public String handle(TcpRequest req) {
        // GET_USER;<id>
        if (req.args() == null || req.args().length != 1) {
            return "ERROR:BAD_ARGS";
        }

        Long id;
        try {
            id = Long.parseLong(req.args()[0].trim());
        } catch (NumberFormatException e) {
            return "ERROR:ID_BAD_FORMAT";
        }

        Erabiltzailea u = service.findById(id).orElse(null);
        if (u == null) return "NOT_FOUND";

        // OK|id|createdAt|email|izena|mota
        return "OK|"
                + u.getId() + "|"
                + formatInstant(u.getCreatedAt()) + "|"
                + safeOut(u.getEmaila()) + "|"
                + safeOut(u.getIzena()) + "|"
                + (u.getMota() != null ? u.getMota().name() : "");
    }

    private String formatInstant(Instant instant) {
        if (instant == null) return "";
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return FORMATTER.format(ldt);
    }

    private String safeOut(String s) {
        if (s == null) return "";
        return s.replace(";", " ").replace("|", " ");
    }
}