package com.zerbitzaria.tcp;

import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import com.zerbitzaria.tcp.util.TcpCodec;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Router de comandos TCP:
 * - Recibe el mensaje en crudo
 * - Lo parsea
 * - Busca el handler correcto (LOGIN/SIGNUP/...)
 * - Devuelve la respuesta
 */
@Component
public class TcpMessageRouter {

    private final Map<String, TcpHandler> handlers = new HashMap<>();

    public TcpMessageRouter(List<TcpHandler> handlerList) {
        // Spring inyecta todos los handlers automáticamente (LoginHandler, SignupHandler...)
        for (TcpHandler h : handlerList) {
            handlers.put(h.command().toUpperCase(), h);
        }
    }

    public String route(String line) {
        try {
            TcpRequest req = TcpCodec.parse(line);

            TcpHandler handler = handlers.get(req.command().toUpperCase());
            if (handler == null) return "MEZU_EZ_EZAGUNA";

            return handler.handle(req);

        } catch (IllegalArgumentException e) {
            return "ERROR;EMPTY";
        } catch (Exception e) {
            return "ERROR;" + e.getClass().getSimpleName();
        }
    }
}