package com.lluisbauza.calipso.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    public static int askInt(String prompt) throws IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print(prompt);
            return sc.nextInt();
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException("It has to be an integer.");
        }
    }

    public static String askString(String prompt) {
        Scanner sc = new Scanner(System.in);
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static boolean askBoolean(String prompt) {
        Scanner sc = new Scanner(System.in);
        System.out.print(prompt);
        return sc.nextBoolean();
    }

    public static LocalDate askLocalDate(String prompt) {
        Scanner sc = new Scanner(System.in);
        System.out.print(prompt);

        try {
            return LocalDate.parse(sc.nextLine());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date. Use the format yyyy-MM-dd."
            );
        }
    }

}
