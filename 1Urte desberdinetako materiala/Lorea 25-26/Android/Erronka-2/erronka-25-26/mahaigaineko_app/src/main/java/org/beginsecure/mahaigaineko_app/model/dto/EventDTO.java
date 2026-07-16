package org.beginsecure.mahaigaineko_app.model.dto;

public class EventDTO {
    private final int id;
    private final String title;
    private final String status;
    private final String description;
    private final String date;
    private final String time;
    private final int stageId;
    private final String price;
    private final String imagePath;

    public EventDTO(int id, String title, String status, String description,
                    String date, String time, int stageId, String price, String imagePath) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        this.date = date;
        this.time = time;
        this.stageId = stageId;
        this.price = price;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getStageId() { return stageId; }
    public String getPrice() { return price; }

    private static String onlyDate(String raw) {
        if (raw == null || raw.isBlank()) return "—";

        String v = raw.trim();
        String[] parts = v.contains("T") ? v.split("T") : v.split(" ");

        return parts.length > 0 ? parts[0] : "—";
    }

    private static String onlyTime(String raw) {
        if (raw == null || raw.isBlank()) return "—";

        String v = raw.trim();
        String[] parts = v.contains("T") ? v.split("T") : v.split(" ");

        if (parts.length < 2) return "—";

        String t = parts[1];
        return t.length() >= 5 ? t.substring(0, 5) : t;
    }

    public String getImagePath() { return imagePath; }

    private static String getAt(String[] f, int idx) {
        return (f != null && idx >= 0 && idx < f.length) ? f[idx] : "";
    }

    /**
     * Zerbitzariaren protokoloaren arabera, event baten eremuen array-tik DTO bat sortzen du.
     * Oharra: indizeak zure jatorrizko controller-etik berdin-berdin hartu dira (portaera ez aldatzeko).
     */
    public static EventDTO fromProtocolFields(String[] f) {
        int eventId = Integer.parseInt(f[0]);
        String title = f[1];
        String status = f[2];
        String description = f[6];

        String rawDateTime = f[4];
        String date = onlyDate(rawDateTime);
        String time = onlyTime(rawDateTime);

        int stageId = Integer.parseInt(f[7].trim());
        String price = f[8] + "€";
        String imagePath = getAt(f, 9);

        return new EventDTO(eventId, title, status, description, date, time, stageId, price,imagePath);
    }
}