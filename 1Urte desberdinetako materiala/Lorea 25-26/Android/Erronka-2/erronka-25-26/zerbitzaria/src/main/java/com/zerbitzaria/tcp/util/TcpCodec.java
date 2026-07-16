package com.zerbitzaria.tcp.util;

import com.zerbitzaria.tcp.protocol.TcpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * TcpCodec se encarga de parsear una línea de texto del socket
 * y convertirla en un TcpRequest.
 *
 * Ejemplo:
 *  "LOGIN;email;pass"
 *  -> command="LOGIN"
 *  -> args=["email", "pass"]
 */
public class TcpCodec {

    // Constructor privado: no queremos instanciar esta clase
    private TcpCodec() {}

    public static TcpRequest parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("EMPTY");
        }
        // Separa el mensaje respetando caracteres escapados (\;, \n, \\)
        List<String> parts = splitEscaped(line);

        if (parts.isEmpty()) {
            throw new IllegalArgumentException("EMPTY");
        }
        // Primer campo = comando
        String cmd = parts.get(0).trim().toUpperCase();
        // El resto = argumentos
        String[] args = parts.subList(1, parts.size()).toArray(new String[0]);

        return new TcpRequest(cmd, args);
    }

    /**
     * Divide por ';' respetando '\;' y desescapa los valores.
     */
    private static List<String> splitEscaped(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaping = false;

        for (char c : input.toCharArray()) {
            if (escaping) {
                current.append(unescape(c));
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == ';') {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result;
    }

    private static char unescape(char c) {
        return switch (c) {
            case 'n' -> '\n';
            case ';' -> ';';
            case '\\' -> '\\';
            default -> c;
        };
    }
}
