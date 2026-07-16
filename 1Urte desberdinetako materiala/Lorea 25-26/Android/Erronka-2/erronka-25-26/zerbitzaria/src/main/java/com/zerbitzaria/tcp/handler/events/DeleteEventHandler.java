package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteEventHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;

    public DeleteEventHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        return "DELETE_EVENT";
    }

    @Override
    public String handle(TcpRequest req) {
        // DELETE_EVENT debe recibir exactamente 1 argumento: el ID
        if (req.args().length != 1) {
            return "ERROR;BAD_ARGS";
        }

        int eventId;
        try {
            eventId = Integer.parseInt(req.args()[0]);
        } catch (NumberFormatException e) {
            return "ERROR;BAD_ID";
        }

        try {
            ekitaldiaService.delete(eventId);
            return "OK";
        } catch (RuntimeException e) {
            return "ERROR;" + e.getMessage();
        } catch (Exception e) {
            return "ERROR;INTERNAL";
        }
    }
}