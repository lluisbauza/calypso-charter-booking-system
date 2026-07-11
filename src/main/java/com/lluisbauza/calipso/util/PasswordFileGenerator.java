package com.lluisbauza.calipso.util;

import com.lluisbauza.calipso.model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PasswordFileGenerator {

    private PasswordFileGenerator() {
    }

    public static void generatePasswordFile(User user, String tempPassword) throws IOException {

        Path outputDirectory = Path.of("generated", "passwords");
        Files.createDirectories(outputDirectory);

        Path outputFile = outputDirectory.resolve(
                "temporary_password_" + user.getUsername() + ".txt"
        );

        try (
                FileWriter file = new FileWriter(outputFile.toFile());
                PrintWriter pw = new PrintWriter(file);
        ) {
            pw.println("************************************************************");
            pw.println("CALIPSO - Temporary Credentials");
            pw.println();
            pw.println("User: " + user.getMail());
            pw.println("Username: " + user.getUsername());
            pw.println("Temporary password: " + tempPassword);
            pw.println();
            pw.println("You'll have to change the password the next time you log in.");
            pw.println("************************************************************");

        }
    }
}
