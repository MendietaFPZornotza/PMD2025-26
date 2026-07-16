package com.zerbitzaria.tcp.handler;

import com.zerbitzaria.tcp.protocol.TcpRequest;

/**
 * Contrato de un handler:
 * - Cada comando TCP (LOGIN, SIGNUP...) tendrá una clase que lo implemente.
 * - command() indica qué comando maneja.
 * - handle(...) recibe TcpRequest y devuelve la respuesta como string.
 */
public interface TcpHandler {
    String command();
    String handle(TcpRequest req);
}