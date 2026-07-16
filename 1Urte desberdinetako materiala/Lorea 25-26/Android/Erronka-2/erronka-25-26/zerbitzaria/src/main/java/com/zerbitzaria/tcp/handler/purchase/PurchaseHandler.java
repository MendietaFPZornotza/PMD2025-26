package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.sarrera.service.ErositakoSarreraService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PurchaseHandler implements TcpHandler {

    private final ErositakoSarreraService sarreraService;

    public PurchaseHandler(ErositakoSarreraService sarreraService) {
        this.sarreraService = sarreraService;
    }

    @Override
    public String command() {
        return "PURCHASE";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 4) return "PURCHASE_FAIL;BAD_ARGS";

        try {
            int userId = Integer.parseInt(req.args()[0].trim());
            int eventId = Integer.parseInt(req.args()[1].trim());
            String email = safeIn(req.args()[2]);     // puede ser ""
            String seatsCsv = safeIn(req.args()[3]);

            if (seatsCsv.isBlank()) return "PURCHASE_FAIL;SEATS_EMPTY";

            List<String> seats = Arrays.stream(seatsCsv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();

            String deskargaKodea = sarreraService.erosiMulti(userId, eventId, seats, email);
            return "PURCHASE_OK;" + deskargaKodea;

        } catch (NumberFormatException nfe) {
            return "PURCHASE_FAIL;BAD_NUMBER";
        } catch (Exception e) {
            return "PURCHASE_FAIL;" + safeOut(shortMsg(e.getMessage()));
        }
    }

    private String safeIn(String s) {
        return s == null ? "" : s.replace(";", " ").trim();
    }

    private String safeOut(String s) {
        return s == null ? "" : s.replace(";", " ").replace("|", " ");
    }

    private String shortMsg(String s) {
        if (s == null) return "SERVER";
        s = s.trim();
        return s.length() > 60 ? s.substring(0, 60) : s;
    }
}