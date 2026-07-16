package com.zerbitzaria.tcp.handler.scenarios;

import com.zerbitzaria.eszenatokia.service.EszenatokiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteScenarioHandler implements TcpHandler {

    private final EszenatokiaService eszenatokiaService;

    public DeleteScenarioHandler(EszenatokiaService eszenatokiaService) {
        this.eszenatokiaService = eszenatokiaService;
    }

    @Override
    public String command() {
        return "DELETE_SCENARIO";
    }

    @Override
    public String handle(TcpRequest req) {
        // DELETE_SCENARIO;id
        if (req.args().length != 1) return "ERROR;BAD_ARGS";

        try {
            int id = Integer.parseInt(req.args()[0]);
            eszenatokiaService.delete(id);
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
}