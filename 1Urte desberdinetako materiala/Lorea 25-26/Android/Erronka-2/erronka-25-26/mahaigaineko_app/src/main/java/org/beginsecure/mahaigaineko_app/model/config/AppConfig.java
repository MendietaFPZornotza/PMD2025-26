package org.beginsecure.mahaigaineko_app.model.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(dotenv.get(key));
    }
}
