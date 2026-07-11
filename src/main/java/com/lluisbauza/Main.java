package com.lluisbauza;

import com.lluisbauza.calipso.dto.ReservationSummary;
import com.lluisbauza.calipso.enums.ReservationOrder;
import com.lluisbauza.calipso.enums.ReservationSearchField;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.service.ReservationService;
import com.lluisbauza.calipso.service.UserService;
import com.lluisbauza.calipso.util.Input;

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
                    if (introduceMail()) {
                        userDashboard();
                    }
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

    public static int openMenu() {
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
                }

                System.out.println("WELCOME");
                return true;

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
                return introduceMail();
            } else if (retry == 2) {
                register(mail);
                return introduceMail();
            }

        }

        return false;
    }

    public static void askNewPassword(String mail) throws Exception {

        String password, newPasswordConfirm;
        boolean success = false;

        while (!success) {

            password = Input.askString("Introduce a new password: ");

            newPasswordConfirm = Input.askString("Type it again: ");
            if (password.equals(newPasswordConfirm)) {
                try {
                    userService.updatePassword(mail, password);
                    System.out.println("Password changed");
                    success = true;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("The passwords do not match.");
            }

        }

    }

    public static void resetPassword(String mail) throws Exception {

        String question = userService.getQuestionByMail(mail);
        System.out.println(question);

        boolean correctAnswer = false;

        while (!correctAnswer) {
            String answer = Input.askString("What's the answer? ");

            correctAnswer = userService.confirmAnswer(mail, answer);

            if (correctAnswer) {
                askNewPassword(mail);
            } else {
                System.out.println("Wrong answer.");
            }
        }

    }

    public static void register(String mail) throws Exception {

        List<SecurityQuestion> securityQuestions = userService.getSecurityQuestions();

        int count = 1;
        for (SecurityQuestion securityQuestion : securityQuestions)
        {
            System.out.println(count + ". " + securityQuestion.getSecurityQuestion());
            count++;
        }

        int choice;

        do {
            choice = Input.askInt("Choose a question: ");

            if (choice < 1 || choice > securityQuestions.size()) {
                System.out.println("Choose a number from 1 to " + securityQuestions.size() + ".");
            }

        } while (choice < 1 || choice > securityQuestions.size());

        SecurityQuestion securityQuestion = securityQuestions.get(choice - 1);

        String securityAnswer = Input.askString("Introduce the answer: ");
        String username = Input.askString("Introduce a username: ");
        String firstName = Input.askString("Name: ");
        String lastName1 = Input.askString("LastName: ");
        String lastName2 = Input.askString("Second Last name: ");

        User user = new User (securityQuestion.getIdSecurityQuestion(), username, firstName,
                lastName1, lastName2, mail, securityAnswer);

        userService.registerUser(user);

        System.out.println("Congratulations, you've registered correctly.");
    }

    private static void userDashboard() throws Exception {
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
                        reservationDashboard();
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

    // RESERVATIONS DASHBOARD

    private static void reservationDashboard() throws Exception {
        int option = 0;

        do {

            System.out.println();
            System.out.println("1. Resrvation Statics.");
            System.out.println("2. All reservations ordered");
            System.out.println("3. Search Reservations");
            System.out.println("4. Exit.");

            try {
                option = Input.askInt("Chose a number from the menu: ");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            switch(option) {
                case 1:
                    reservationsBasicStatistics();
                    break;
                case 2:
                    getAllReservationsOrderedBy();
                    break;
                case 3:
                    findReservationSummaryByField();
                    break;
                case 4:
                    System.out.println("Bye");
                    break;
                default:
                    System.out.println("Choose from 1 to 4.");
            }

        } while (option != 4);

    }

    private static void reservationsBasicStatistics() throws SQLException, ClassNotFoundException {
        System.out.println();
        System.out.println("Total upcoming reservations: " + reservationService.upcomingReservations());
        System.out.println("Total reservations in the last month: " + reservationService.lastMonthReservations());

        Map<String, Integer> reservationsPerBoat = reservationService.upcomingReservationsPerBoat();

        System.out.println("Upcoming reservations per boat: ");
        for (String boatName : reservationsPerBoat.keySet()) {
            System.out.print("-" + boatName + ": ");
            System.out.println(reservationsPerBoat.get(boatName));
        }

        Map<String, Integer> reservationsPerTripType = reservationService.upcomingReservationsPerType();

        System.out.println("Upcoming reservations per type: ");

        for (String tripOption : reservationsPerTripType.keySet()) {
            System.out.print("-" + tripOption + ": " );
            System.out.println(reservationsPerTripType.get(tripOption));
        }

    }

    private static void getAllReservationsOrderedBy() throws Exception {

        ReservationOrder[] options = ReservationOrder.values();

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int option = Input.askInt("Choose: ");

        if (option < 1 || option > options.length) {
            throw new IllegalArgumentException("Invalid option.");
        }

        ReservationOrder order = options[option - 1];

        List<ReservationSummary> summaries = reservationService.listReservationSummariesOrderedBy(order);

        for (ReservationSummary summary : summaries) {
            System.out.println(summary);
        }
    }

    private static void findReservationSummaryByField() throws Exception {
        ReservationSearchField[] fields = ReservationSearchField.values();

        for (int i = 0; i < fields.length; i++) {
            System.out.println((i + 1) + ". " + fields[i]);
        }
        int option = Input.askInt("Choose: ");

        if (option < 1 || option > fields.length) {
            throw new IllegalArgumentException("Invalid option.");
        }

        ReservationSearchField searchField = fields[option - 1];

        String value = Input.askString("Introduce a value: ");

        List<ReservationSummary> summaries = reservationService.findReservationSummaryByField(searchField, value);

        for (ReservationSummary summary : summaries) {
            System.out.println(summary);
        }
    }


}