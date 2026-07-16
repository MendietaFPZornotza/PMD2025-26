package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

import java.util.HashMap;
import java.util.Map;

public class PurchaseClient extends TcpClient {

    /**
     * GET_PURCHASE_COUNT;eventId -> OK;count
     */
    public static int getPurchasedCountByEvent(int eventId) throws Exception {
        String response = sendMessage("GET_PURCHASE_COUNT;" + eventId);

        if (response == null) throw new Exception("Empty response");
        if (!response.startsWith("OK")) throw new Exception(response);

        String[] parts = response.split(";", 2);
        if (parts.length < 2) return 0;

        try {
            return Integer.parseInt(parts[1].trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * (Opcional) Batch: GET_PURCHASE_COUNTS;id1|id2|id3 -> OK;id|count;id|count;...
     * Si luego lo implementas en servidor, aquí lo tienes listo.
     */
    public static Map<Integer, Integer> getPurchasedCountsBatch(int[] eventIds) throws Exception {
        if (eventIds == null || eventIds.length == 0) return Map.of();

        StringBuilder sb = new StringBuilder("GET_PURCHASE_COUNTS;");
        for (int i = 0; i < eventIds.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(eventIds[i]);
        }

        String response = sendMessage(sb.toString());
        if (response == null) throw new Exception("Empty response");
        if (!response.startsWith("OK")) throw new Exception(response);

        Map<Integer, Integer> out = new HashMap<>();
        String[] parts = response.split(";", 2);
        if (parts.length < 2 || parts[1].isBlank()) return out;

        String[] items = parts[1].split(";");
        for (String it : items) {
            if (it == null || it.isBlank()) continue;
            String[] f = it.split("\\|", -1);
            if (f.length < 2) continue;
            try {
                int id = Integer.parseInt(f[0].trim());
                int c = Integer.parseInt(f[1].trim());
                out.put(id, c);
            } catch (Exception ignore) {}
        }
        return out;
    }
}
