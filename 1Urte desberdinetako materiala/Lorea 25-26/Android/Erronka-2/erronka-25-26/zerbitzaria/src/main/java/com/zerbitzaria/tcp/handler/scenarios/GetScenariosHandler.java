package com.zerbitzaria.tcp.handler.scenarios;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.eszenatokia.service.EszenatokiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetScenariosHandler implements TcpHandler {

    private final EszenatokiaService eszenatokiaService;

    public GetScenariosHandler(EszenatokiaService eszenatokiaService) {
        this.eszenatokiaService = eszenatokiaService;
    }

    @Override
    public String command() {
        return "GET_SCENARIOS";
    }

    @Override
    public String handle(TcpRequest req) {
        try {
            List<Eszenatokia> list = eszenatokiaService.findAll();
            if (list.isEmpty()) return "OK";

            // OK;id|izena|lekua|aforoa|emaila|telefonoa;...
            StringBuilder sb = new StringBuilder("OK");
            for (Eszenatokia e : list) {
                sb.append(";")
                        .append(e.getId()).append("|")
                        .append(nullToEmpty(e.getIzena())).append("|")
                        .append(nullToEmpty(e.getLekua())).append("|")
                        .append(e.getAforoa()).append("|")
                        .append(nullToEmpty(e.getEmaila())).append("|")
                        .append(nullToEmpty(e.getTelefonoa()));
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR;INTERNAL";
        }
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}