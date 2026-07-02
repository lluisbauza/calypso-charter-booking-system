package com.lluisbauza.calipso.util;

import java.security.SecureRandom;

public final class PasswordGenerator {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private PasswordGenerator() {
    }

    public static String generateTempPassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();

    }

}
