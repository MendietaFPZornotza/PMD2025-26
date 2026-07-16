package com.zerbitzaria.tcp.handler.events.support;

public final class EventEscaper {

    private EventEscaper() {}

    /**
     * Admin panelean erabiltzen den escape-a (bezeroak split egiten duenean ez apurtzeko).
     */
    public static String escapeAdmin(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    /**
     * Mugikorrean erabiltzen den "safe" bertsioa (delimitadoreak ezabatu/ordezkatu).
     */
    public static String safeClient(String s) {
        if (s == null) return "";
        return s.replace(";", " ").replace("|", " ");
    }
}