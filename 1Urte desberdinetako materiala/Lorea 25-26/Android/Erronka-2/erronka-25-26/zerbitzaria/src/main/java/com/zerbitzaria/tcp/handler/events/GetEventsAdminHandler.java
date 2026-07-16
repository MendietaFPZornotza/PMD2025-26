package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.handler.events.support.EventProtocolFormatter;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class GetEventsAdminHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;

    public GetEventsAdminHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        // Komandoa EZ da aldatzen (admin panelak hau bidaltzen du).
        return "GET_EVENTS_A";
    }

    @Override
    public String handle(TcpRequest req) {
        // Admin panelerako zerrenda osoa
        return EventProtocolFormatter.toAdminGetEventsResponse(ekitaldiaService.findAll());
    }
}