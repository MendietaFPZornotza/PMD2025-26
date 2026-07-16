package com.zerbitzaria.tcp.handler.scenarios;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.eszenatokia.service.EszenatokiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class NewScenarioHandler implements TcpHandler {

    private final EszenatokiaService eszenatokiaService;

    public NewScenarioHandler(EszenatokiaService eszenatokiaService) {
        this.eszenatokiaService = eszenatokiaService;
    }

    @Override
    public String command() {
        return "NEW_SCENARIO";
    }

    @Override
    public String handle(TcpRequest req) {
        // NEW_SCENARIO;izena;lekua;aforoa;emaila;telefonoa
        if (req.args().length != 5) return "ERROR;BAD_ARGS";

        try {
            Eszenatokia e = new Eszenatokia();
            e.setIzena(req.args()[0]);
            e.setLekua(req.args()[1]);
            e.setAforoa(Integer.parseInt(req.args()[2]));
            e.setEmaila(emptyToNull(req.args()[3]));
            e.setTelefonoa(emptyToNull(req.args()[4]));

            Eszenatokia created = eszenatokiaService.create(e);
            return "OK;" + created.getId();

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