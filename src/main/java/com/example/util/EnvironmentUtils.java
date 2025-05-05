package com.example.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentUtils {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getEnv(String key) {
        return dotenv.get(key);
    }
}