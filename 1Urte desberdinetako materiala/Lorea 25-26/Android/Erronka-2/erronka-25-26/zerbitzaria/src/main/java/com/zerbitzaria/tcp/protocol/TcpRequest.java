package com.zerbitzaria.tcp.protocol;

/**
 * TcpRequest representa una petición TCP ya parseada.
 * command = comando (LOGIN, SIGNUP...)
 * args = argumentos del comando
 */
public record TcpRequest(String command, String[] args) {}
