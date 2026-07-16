package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.common.enums.EkitaldiMota;
import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class UpdateEventHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;

    public UpdateEventHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        return "UPDATE_EVENT";
    }

    @Override
    public String handle(TcpRequest req) {

        // UPDATE_EVENT;id;title;mota;generoa;hasiera;amaiera;sinopsia;eszenatokiaId
        if (req.args().length != 10) {
            return "ERROR;BAD_ARGS";
        }

        try {
            int id = Integer.parseInt(req.args()[0]);

            Ekitaldia updated = new Ekitaldia();
            updated.setIzenburua(req.args()[1]);
            updated.setMota(EkitaldiMota.valueOf(req.args()[2]));
            updated.setGeneroa(req.args()[3]);
            updated.setHasiera(LocalDateTime.parse(req.args()[4]));
            updated.setAmaiera(LocalDateTime.parse(req.args()[5]));
            updated.setSinopsia(req.args()[6]);

            int eszenatokiaId = Integer.parseInt(req.args()[7]);
            BigDecimal prezioa = parsePrice(req.args()[8]);
            updated.setPrezioa(prezioa);
            String imagePath = (req.args()[9]);
            updated.setArgazkia(imagePath);

            ekitaldiaService.update(id, updated, eszenatokiaId);

            return "OK";

        } catch (IllegalArgumentException e) {
            return "ERROR;BAD_VALUE";
        } catch (RuntimeException e) {
            return "ERROR;" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR;INTERNAL";
        }
    }

    private BigDecimal parsePrice(String raw) {
        if (raw == null) return BigDecimal.ZERO;

        String s = raw.trim();
        if (s.isEmpty()) return BigDecimal.ZERO;

        // kendu euro ikurra eta espazio arraroak
        s = s.replace("€", "").trim();

        // koma -> puntua (EU formatu ohikoa)
        s = s.replace(",", ".");

        return new BigDecimal(s);
    }
}
