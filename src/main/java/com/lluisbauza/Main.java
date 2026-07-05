package com.lluisbauza;

import com.lluisbauza.calipso.dao.SecurityQuestionDao;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.service.ReservationService;
import com.lluisbauza.calipso.service.UserService;
import com.lluisbauza.calipso.util.Input;
import com.lluisbauza.calipso.util.PasswordFileGenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {

    private static UserService userService;
    private static ReservationService reservationService;

    public static void main(String[] args) throws Exception {

        userService = new UserService();
        reservationService = new ReservationService();

        int option;
        do {
            option = openMenu();
            switch(option) {
                case 1:
                    introduceMail();
                    userDashboard();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("Bye");
                    break;
                default:
                    System.out.println("Choose from 1 to 4.");
            }

        } while (option != 4);

    }

    public static int openMenu() throws Exception {
        int option = 0;

        System.out.println();
        System.out.println("1. User.");
        System.out.println("2. .");
        System.out.println("3. ");
        System.out.println("4. Exit.");

        try {
            option = Input.askInt("Chose a number from the menu: ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return option;
    }

    // USER, PASSWORD AND LOG IN MANAGEMENT

    public static boolean introduceMail() throws Exception {

        String mail = Input.askString("Mail: ");

        if (userService.userExistsByMail(mail)){

            String password = Input.askString("Password: ");

            if (userService.login(mail, password)) {
                System.out.println("CORRECT CREDENTIALS.");

                if (userService.checkPasswordNeedsChange(mail)) {
                    askNewPassword(mail);
                } else {
                    System.out.println("WELCOME");
                    return true;
                }

            } else {
                System.out.println("Incorrect password.");
                System.out.println("Reset password.");
                resetPassword(mail);
            }

        } else {
            System.out.println("User not found.");
            System.out.println("What do you wanna do?");
            System.out.println("1. Try again?");
            System.out.println("2. Create an account?");

            int retry = Input.askInt("What do you wanna do: ");
            if (retry == 1) {
                introduceMail();
            } else if (retry == 2) {
                register(mail);
            }

        }

        return false;
    }

    public static void askNewPassword(String mail) throws Exception {

        String password, newPasswordConfirm;
        boolean success = false;

        while (!success) {

            password = Input.askString("Introduce a new password: ");

            try {
                userService.isPasswordValid(password);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            if (!userService.isPasswordNew(mail, password)) {
                System.out.println("You can't use a password you've used in the past.");
                continue;
            }

            newPasswordConfirm = Input.askString("Type it again: ");
            if (password.equals(newPasswordConfirm)) {
                userService.updatePassword(mail, password);
                System.out.println("Password changed");
                success = true;
            } else {
                System.out.println("The passwords do not match.");
            }

        }

    }

    public static void resetPassword(String mail) throws Exception {

        String question = userService.getQuestionByMail(mail);
        System.out.println(question);
        String answer = Input.askString("What's the answer? ");

        if (userService.confirmAnswer(mail, answer)) {
            askNewPassword(mail);
        }

    }

    public static void register(String mail) throws Exception {

        SecurityQuestionDao securityQuestionDao = new SecurityQuestionDao();
        List<SecurityQuestion> securityQuestions = securityQuestionDao.listAll();

        int count = 1;
        for (SecurityQuestion securityQuestion : securityQuestions)
        {
            System.out.println(count + ". " + securityQuestion.getSecurityQuestion());
            count++;
        }

        int choice = Input.askInt("Choose a question: ");

        SecurityQuestion securityQuestion = securityQuestions.get(choice - 1);

        String securityAnswer = Input.askString("Introduce the answer: ");
        String username = Input.askString("Introduce a username: ");
        String firstName = Input.askString("Name: ");
        String lastName1 = Input.askString("LastName: ");
        String lastName2 = Input.askString("Second Last name: ");

        User user = new User (securityQuestion.getIdSecurityQuestion(), username, firstName,
                lastName1, lastName2, mail, securityAnswer);

        String tempPassword = userService.registerUser(user);

        generateTempPasswordFile(user, tempPassword);

        System.out.println("Congratulations, you've registered correctly.");
    }

    public static void generateTempPasswordFile(User user, String tempPassword) throws IOException {

        try {
            PasswordFileGenerator.generatePasswordFile(user, tempPassword);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void userDashboard() throws SQLException, ClassNotFoundException {
        int option = 0;

        do {

            System.out.println();
            System.out.println("1. Reservations.");
            System.out.println("2. Agencies");
            System.out.println("3. Clients");
            System.out.println("4. Exit.");

            try {
                option = Input.askInt("Chose a number from the menu: ");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

                switch(option) {
                    case 1:
                        reservationInfo();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Bye");
                        break;
                    default:
                        System.out.println("Choose from 1 to 4.");
                }

        } while (option != 4);

    }

    private static void reservationInfo() throws SQLException, ClassNotFoundException {
        System.out.println();
        System.out.println("Total upcoming reservations: " + reservationService.upcomingReservations());
        System.out.println("Total reservations in the last month: " + reservationService.lastMonthReservations());

        Map<String, Integer> reservationsPerBoat = reservationService.upcomingReservationsPerBoat();

        System.out.println("Upcoming reservations per boat: ");
        for (String boatName : reservationsPerBoat.keySet()) {
            System.out.print("-" + boatName.toString() + ": ");
            System.out.println(reservationsPerBoat.get(boatName).toString());
        }

        Map<String, Integer> reservationsPerTripType = reservationService.upcomingReservationsPerType();

        System.out.println("Upcoming reservations per type: ");

        for (String tripOption : reservationsPerTripType.keySet()) {
            System.out.print("-" + tripOption.toString() + ": " );
            System.out.println(reservationsPerTripType.get(tripOption).toString());
        }

    }

}