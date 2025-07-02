package com.java.admin.util;

import java.util.Random;

public class GenerateRandomDataUtil {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 7;
    private static final int CODE_LENGTH = 6; // Default length for generated codes
    static Random random = new Random();

    private GenerateRandomDataUtil() {
        // Private constructor to prevent instantiation
    }

    public static String generateShortUrl(long id) {
        // Usar el ID como semilla para el generador aleatorio
        Random random = new Random(id);
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);

        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }

    public static String generateCode() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS_FOR_CODE.length());
            code.append(CHARACTERS_FOR_CODE.charAt(index));
        }

        return code.toString();
    }
}
