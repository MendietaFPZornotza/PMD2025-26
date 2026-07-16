package com.zerbitzaria.tcp.handler.events.support;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;

import java.time.format.DateTimeFormatter;
import java.util.List;

public final class EventProtocolFormatter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private EventProtocolFormatter() {}

    /**
     * Admin-erako: OK;id|title|type|genre|start|end|synopsis|stageId|price|argazkia|
     */
    public static String toAdminGetEventsResponse(List<Ekitaldia> events) {
        if (events == null || events.isEmpty()) return "OK";

        StringBuilder sb = new StringBuilder("OK");
        for (Ekitaldia e : events) {
            sb.append(";")
                    .append(e.getId()).append("|")
                    .append(EventEscaper.escapeAdmin(e.getIzenburua())).append("|")
                    .append(EventEscaper.escapeAdmin(e.getMota() != null ? e.getMota().name() : "")).append("|")
                    .append(EventEscaper.escapeAdmin(e.getGeneroa())).append("|")
                    .append(e.getHasiera()).append("|")
                    .append(e.getAmaiera()).append("|")
                    .append(EventEscaper.escapeAdmin(e.getSinopsia())).append("|")
                    .append(e.getEszenatokia() != null ? e.getEszenatokia().getId() : 0).append("|")
                    .append(e.getPrezioa()).append("|")
                    .append(EventEscaper.escapeAdmin(e.getArgazkia())).append("|");
        }
        return sb.toString();
    }

    /**
     * Bezero (mugikor) app-erako:
     * EVENTS_OK;N;id|title|genre|date|time|room|price|synopsis|aforo
     */
    public static String toClientEventListResponse(List<Ekitaldia> list) {
        if (list == null || list.isEmpty()) return "EVENTS_EMPTY";

        StringBuilder sb = new StringBuilder("EVENTS_OK;").append(list.size());

        for (Ekitaldia e : list) {
            String room = (e.getEszenatokia() != null)
                    ? EventEscaper.safeClient(e.getEszenatokia().getIzena())
                    : "-";

            String date = e.getHasiera().format(DATE_FMT);
            String time = e.getHasiera().format(TIME_FMT);

            // Aforoa eszenatokitik hartzen da (ekitaldiak ez duelako eremu hori).
            int aforoa = (e.getEszenatokia() != null && e.getEszenatokia().getAforoa() != null)
                    ? e.getEszenatokia().getAforoa()
                    : 0;

            sb.append(";")
                    .append(e.getId() == null ? 0 : e.getId()).append("|")
                    .append(EventEscaper.safeClient(e.getIzenburua())).append("|")
                    .append(EventEscaper.safeClient(e.getGeneroa())).append("|")
                    .append(date).append("|")
                    .append(time).append("|")
                    .append(room).append("|")
                    .append(e.getPrezioa() != null ? e.getPrezioa().toPlainString() : "0.0").append("|")
                    .append(EventEscaper.safeClient(e.getSinopsia())).append("|")
                    .append(aforoa);
        }

        return sb.toString();
    }
}