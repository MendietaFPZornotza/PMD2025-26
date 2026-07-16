package com.zerbitzaria.tcp.handler.scenarios;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.eszenatokia.service.EszenatokiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateScenarioHandler implements TcpHandler {

    private final EszenatokiaService eszenatokiaService;

    public UpdateScenarioHandler(EszenatokiaService eszenatokiaService) {
        this.eszenatokiaService = eszenatokiaService;
    }

    @Override
    public String command() {
        return "UPDATE_SCENARIO";
    }

    @Override
    public String handle(TcpRequest req) {
        // UPDATE_SCENARIO;id;izena;lekua;aforoa;emaila;telefonoa
        if (req.args().length != 6) return "ERROR;BAD_ARGS";

        try {
            int id = Integer.parseInt(req.args()[0]);

            Eszenatokia updated = new Eszenatokia();
            updated.setIzena(req.args()[1]);
            updated.setLekua(req.args()[2]);
            updated.setAforoa(Integer.parseInt(req.args()[3]));
            updated.setEmaila(emptyToNull(req.args()[4]));
            updated.setTelefonoa(emptyToNull(req.args()[5]));

            eszenatokiaService.update(id, updated);
            return "OK";

        } catch (NumberFormatException ex) {
            return "ERROR;BAD_VALUE";
        } catch (RuntimeException ex) {
            return "ERROR;" + ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR;INTERNAL";
        }
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}