package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

public class EventClient extends TcpClient {

    /**
     * Ekitaldi berria sortzeko mezua bidaltzen du zerbitzarira.
     * Oharra: eremuak "clean" egiten dira delimitadoreekin arazoak ekiditeko.
     */
    public static String sendEvent(
            String izenburua,
            String mota,
            String generoa,
            String hasiera,
            String amaiera,
            String sinopsia,
            Integer eszenatokiId,
            String price,
            String imagePath
    ) throws Exception {

        String message = String.join(";",
                "NEW_EVENT",
                clean(izenburua),
                clean(mota),
                clean(generoa),
                clean(hasiera),
                clean(amaiera),
                clean(sinopsia),
                String.valueOf(eszenatokiId),
                clean(price),
                clean(imagePath)
        );

        return sendMessage(message);
    }

    /**
     * Zerbitzari-protokoloak ";" erabiltzen duenez, testuak garbitzen ditu:
     * - "\" ihes karaktereak bikoiztu
     * - ";" -> "\;"
     * - lerro-jauziak sinbolo bihurtu
     */
    private static String clean(String s) {
        if (s == null) return "";
        return s
                .replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    /**
     * Ekitaldi guztiak eskatzen ditu.
     */
    public static String getEvents() throws Exception {
        return sendMessage("GET_EVENTS_A");
    }

    /**
     * Ekitaldi bat ezabatzeko eskaera.
     */
    public static String deleteEvent(int eventId) throws Exception {
        return sendMessage("DELETE_EVENT;" + eventId);
    }

    /**
     * Ekitaldi bat eguneratzeko eskaera.
     */
    public static String updateEvent(
            int eventId,
            String title,
            String type,
            String genre,
            String start,
            String end,
            String synopsis,
            int eszenatokiId,
            String price,
            String imagePath
    ) {
        try {
            String message = String.join(";",
                    "UPDATE_EVENT",
                    String.valueOf(eventId),
                    clean(title),
                    clean(type),
                    clean(genre),
                    clean(start),
                    clean(end),
                    clean(synopsis),
                    String.valueOf(eszenatokiId),
                    clean(price),
                    clean(imagePath)
            );

            return sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * ID bidez ekitaldi baten xehetasunak eskatzen ditu.
     */
    public static String getEventById(int eventId) throws Exception {
        return sendMessage("GET_EVENT;" + eventId);
    }
}