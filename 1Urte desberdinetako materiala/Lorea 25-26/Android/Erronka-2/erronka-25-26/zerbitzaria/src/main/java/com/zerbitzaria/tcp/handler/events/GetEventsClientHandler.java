package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.common.enums.EkitaldiMota;
import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.handler.events.support.EventProtocolFormatter;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class GetEventsClientHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;

    public GetEventsClientHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        // Komandoa EZ da aldatzen (bezero app-ak hau bidaltzen du).
        return "GET_EVENTS_B";
    }

    @Override
    public String handle(TcpRequest req) {
        // EVENT_LIST;category
        if (req.args() == null || req.args().length != 1) return "EVENTS_FAIL;BAD_ARGS";

        EkitaldiMota mota;
        try {
            mota = mapCategoryToMota(req.args()[0].trim().toUpperCase());
        } catch (Exception e) {
            return "EVENTS_FAIL;BAD_CATEGORY";
        }

        return EventProtocolFormatter.toClientEventListResponse(
                ekitaldiaService.findUpcomingByMota(mota)
        );
    }

    /**
     * Mugikorreko kategoriak enum-era mapatzen ditu.
     */
    private EkitaldiMota mapCategoryToMota(String cat) {
        return switch (cat) {
            case "MOVIES" -> EkitaldiMota.PELIKULA;
            case "THEATRE" -> EkitaldiMota.ANTZERKIA;
            case "CONCERTS" -> EkitaldiMota.KONTZERTUA;
            default -> EkitaldiMota.valueOf(cat);
        };
    }
}