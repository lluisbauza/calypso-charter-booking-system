package com.lluisbauza.calipso.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    public static int askInt(String prompt) throws Exception {
        Scanner sc = new Scanner(System.in);
        int integer;
        try {
            System.out.print(prompt);
            integer = sc.nextInt();
        } catch (InputMismatchException e) {
            throw new Exception ("It has to be an integer.");
        }
        return integer;
    }

    public static String askString(String prompt) {
        Scanner sc = new Scanner(System.in);
        String string;

        System.out.print(prompt);
        string = sc.nextLine();

        return string;
    }

}
