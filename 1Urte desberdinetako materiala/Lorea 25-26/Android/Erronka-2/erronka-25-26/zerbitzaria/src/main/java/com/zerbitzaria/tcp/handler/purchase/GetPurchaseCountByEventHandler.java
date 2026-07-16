package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.sarrera.service.ErositakoSarreraService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class GetPurchaseCountByEventHandler implements TcpHandler {

    private final ErositakoSarreraService service;

    public GetPurchaseCountByEventHandler(ErositakoSarreraService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "GET_PURCHASE_COUNT"; //GET_PURCHASE_COUNT_BY_EVENT
    }

    @Override
    public String handle(TcpRequest req) {
        // Argumentu bakarra espero dugu: eventId
        if (req.args().length != 1) return "ERROR;BAD_ARGS";

        String raw = req.args()[0];
        if (raw == null || raw.trim().isEmpty()) return "ERROR;EMPTY";

        int eventId;
        try {
            eventId = Integer.parseInt(raw.trim());
        } catch (Exception e) {
            return "ERROR;BAD_ID";
        }

        try {
            // Service-etik kontagailua hartu
            long count = service.countByEkitaldia(eventId);

            // Bezeroak espero du: OK;count
            return "OK;" + count;

        } catch (Exception e) {
            // Hemen ez dugu stacktrace-a bezeroari bidali nahi; mezua laburtu
            return "ERROR;SERVER_FAIL";
        }
    }
}
