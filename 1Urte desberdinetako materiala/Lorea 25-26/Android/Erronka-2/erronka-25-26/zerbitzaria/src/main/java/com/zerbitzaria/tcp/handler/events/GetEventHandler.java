package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class GetEventHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;
    private static final String IMAGE_BASE_URL =
            "http://192.168.5.25:8080/home/zornotza/Dokumentuak/Argazkiak/";


    public GetEventHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        return "GET_EVENT";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 1) {
            return "ERROR;BAD_ARGS";
        }

        try {
            int id = Integer.parseInt(req.args()[0]);
            Ekitaldia e = ekitaldiaService.findById(id);

            String imageUrl = "";

            if (e.getArgazkia() != null && !e.getArgazkia().isBlank()) {
                imageUrl = IMAGE_BASE_URL + e.getArgazkia();
            }


            return String.join("|",
                    "OK",
                    String.valueOf(e.getId()),
                    escape(e.getIzenburua()),
                    e.getMota().name(),
                    escape(e.getGeneroa()),
                    e.getHasiera().toString(),
                    e.getAmaiera().toString(),
                    escape(e.getSinopsia()),
                    String.valueOf(e.getEszenatokia().getId()),
                    e.getPrezioa().toString(),
                    escape(imageUrl)
            );

        } catch (Exception ex) {
            return "ERROR;" + ex.getMessage();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace(";", "\\;");
    }
}
