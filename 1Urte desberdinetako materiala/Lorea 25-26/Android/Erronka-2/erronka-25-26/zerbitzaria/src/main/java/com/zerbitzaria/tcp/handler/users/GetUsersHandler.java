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
import java.util.List;

@Component
public class GetUsersHandler implements TcpHandler {

    private final ErabiltzaileaService service;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public GetUsersHandler(ErabiltzaileaService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "GET_USERS";
    }

    @Override
    public String handle(TcpRequest req) {
        // GET_USERS (ez du argumenturik)
        List<Erabiltzailea> users = service.findAll();

        if (users.isEmpty()) return "OK";

        StringBuilder sb = new StringBuilder("OK");

        for (Erabiltzailea u : users) {
            sb.append(";")
                    .append(u.getId()).append("|")
                    .append(formatInstant(u.getCreatedAt())).append("|")
                    .append(safeOut(u.getEmaila())).append("|")
                    .append(safeOut(u.getIzena())).append("|")
                    .append(u.getMota() != null ? u.getMota().name() : "");
        }
        return sb.toString();
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