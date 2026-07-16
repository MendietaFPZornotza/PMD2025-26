package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScenarioClient extends TcpClient {

    // id, izena, lekua, aforoa, emaila, telefonoa
    public record ScenarioDTO(int id, String izena, String lekua, int aforoa, String emaila, String telefonoa) {}

    /**
     * Lista completa: id, izena, lekua, aforoa, emaila, telefonoa
     * Server: OK;id|izena|lekua|aforoa|emaila|telefonoa;...
     */
    public static List<ScenarioDTO> getScenarios() throws Exception {
        String response = sendMessage("GET_SCENARIOS");

        if (!response.startsWith("OK")) throw new Exception(response);

        String[] parts = response.split(";", 2);
        if (parts.length < 2 || parts[1].isBlank()) return new ArrayList<>();

        List<ScenarioDTO> list = new ArrayList<>();
        String[] items = parts[1].split(";");

        for (String item : items) {
            if (item == null || item.isBlank()) continue;

            String[] f = item.split("\\|", -1);
            // id|izena|lekua|aforoa|emaila|telefonoa
            if (f.length < 6) continue;

            int id = parseIntSafe(f[0], -1);
            if (id == -1) continue;

            String izena = nullToEmpty(f[1]);
            String lekua = nullToEmpty(f[2]);
            int aforoa = parseIntSafe(f[3], 0);
            String emaila = nullToEmpty(f[4]);
            String telefonoa = nullToEmpty(f[5]);

            list.add(new ScenarioDTO(id, izena, lekua, aforoa, emaila, telefonoa));
        }

        return list;
    }

    /**
     * Mantiene lo antiguo: (id -> izena)
     */
    public static Map<Integer, String> getScenariosMap() throws Exception {
        List<ScenarioDTO> list = getScenarios();
        Map<Integer, String> map = new LinkedHashMap<>();
        for (ScenarioDTO s : list) map.put(s.id(), s.izena());
        return map;
    }

    /**
     * NEW_SCENARIO;izena;lekua;aforoa;emaila;telefonoa
     * -> OK;newId
     */
    public static int newScenario(String izena, String lekua, int aforoa, String emaila, String telefonoa) throws Exception {
        String msg = "NEW_SCENARIO;"
                + safeArg(izena) + ";"
                + safeArg(lekua) + ";"
                + aforoa + ";"
                + safeArg(emaila) + ";"
                + safeArg(telefonoa);

        String response = sendMessage(msg);
        if (!response.startsWith("OK")) throw new Exception(response);

        String[] parts = response.split(";", 2);
        if (parts.length < 2) return -1;
        return parseIntSafe(parts[1], -1);
    }

    /**
     * UPDATE_SCENARIO;id;izena;lekua;aforoa;emaila;telefonoa
     * -> OK
     */
    public static void updateScenario(int id, String izena, String lekua, int aforoa, String emaila, String telefonoa) throws Exception {
        String msg = "UPDATE_SCENARIO;"
                + id + ";"
                + safeArg(izena) + ";"
                + safeArg(lekua) + ";"
                + aforoa + ";"
                + safeArg(emaila) + ";"
                + safeArg(telefonoa);

        String response = sendMessage(msg);
        if (!response.startsWith("OK")) throw new Exception(response);
    }

    /**
     * DELETE_SCENARIO;id
     * -> OK
     */
    public static void deleteScenario(int id) throws Exception {
        String response = sendMessage("DELETE_SCENARIO;" + id);
        if (!response.startsWith("OK")) throw new Exception(response);
    }

    // =====================
    // Helpers
    // =====================

    private static int parseIntSafe(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String safeArg(String s) {
        return (s == null) ? "" : s;
    }
}