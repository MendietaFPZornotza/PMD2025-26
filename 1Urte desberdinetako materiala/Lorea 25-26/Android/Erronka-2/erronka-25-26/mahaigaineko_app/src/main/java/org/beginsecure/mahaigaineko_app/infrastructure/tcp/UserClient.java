package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

import org.beginsecure.mahaigaineko_app.model.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserClient extends TcpClient {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Erabiltzaile guztiak TCP bidez eskatzen ditu eta lista moduan bueltatzen du.
     * Protokoloa: GET_USERS -> OK;id|createdAt|email|izena|mota;...
     */
    public static List<User> getUsers() throws Exception {
        String response = sendMessage("GET_USERS");
        if (!response.startsWith("OK")) {
            throw new Exception(response);
        }

        List<User> result = new ArrayList<>();
        String[] parts = response.split(";", 2);
        if (parts.length < 2) return result;

        String[] items = parts[1].split(";");
        for (String item : items) {
            if (item == null || item.isBlank()) continue;

            String[] data = item.split("\\|", -1);
            Integer id = Integer.parseInt(data[0]);
            LocalDateTime createdAt = data[1].isEmpty() ? null : LocalDateTime.parse(data[1], FORMATTER);
            String emaila = data[2];
            String izena = data[3];
            String mota = data[4];

            result.add(new User(id, createdAt, emaila, izena, mota));
        }

        return result;
    }

    /**
     * Erabiltzaile bat sortzen du.
     * Protokoloa: NEW_USER;izena;emaila;pass;mota -> OK
     */
    public static boolean newUser(String izena, String emaila, String pass, String mota) throws Exception {
        String message = String.join(";",
                "NEW_USER",
                clean(izena),
                clean(emaila),
                clean(pass),
                clean(mota)
        );

        String response = sendMessage(message);

        if ("OK".equals(response)) return true;
        if (response != null && response.startsWith("ERROR")) throw new Exception(response);
        throw new Exception("Unexpected response: " + response);
    }

    /**
     * Erabiltzaile bat eguneratzen du.
     * Protokoloa: UPDATE_USER;id;izenaOrBlank;emailaOrBlank;passOrBlank;motaOrBlank -> OK
     */
    public static boolean updateUser(Integer id, String izenaOrBlank, String emailaOrBlank,
                                     String passOrBlank, String motaOrBlank) throws Exception {

        String message = String.join(";",
                "UPDATE_USER",
                String.valueOf(id),
                clean(izenaOrBlank),
                clean(emailaOrBlank),
                clean(passOrBlank),
                clean(motaOrBlank)
        );

        String response = sendMessage(message);

        if ("OK".equals(response)) return true;
        if (response != null && response.startsWith("ERROR")) throw new Exception(response);
        throw new Exception("Unexpected response: " + response);
    }

    /**
     * Erabiltzaile bat ezabatzeko eskaera bidaltzen du.
     * Protokoloa: DELETE_USER;id -> OK / ERROR:...
     */
    public static boolean deleteUser(Integer id) throws Exception {
        String response = sendMessage("DELETE_USER;" + id);

        if (response.startsWith("OK")) {
            return true;
        } else if (response.startsWith("ERROR")) {
            // "ERROR:..." edo "ERROR;..." izan daiteke; hemen mezua pasatzen dugu.
            throw new Exception(response);
        } else {
            return false;
        }
    }

    /**
     * TCP protokoloa ez apurtzeko karaktere bereziak “ihes” egiten ditu.
     * - ";" eta "|" delimitadoreak dira
     * - "\n" / "\r" ere garbitu
     */
    private static String clean(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}