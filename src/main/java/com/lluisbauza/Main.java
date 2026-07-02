package com.lluisbauza;

import com.lluisbauza.calipso.dao.SecurityQuestionDao;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.service.UserService;
import com.lluisbauza.calipso.util.Input;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception, SQLException, ClassNotFoundException {
//        AgencyService agencyService = new AgencyService();
//        Agency agency = new Agency("W56789345", "Venganza", 10);
//        agencyService.createAgency(agency);

        int option;
        do {
            option = openMenu();
            switch(option) {
                case 1:
                    introduceMail();
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

    public static void introduceMail() throws Exception {

        UserService userService = new UserService();

        String mail = Input.askString("Mail: ");

        if (userService.userExistsByMail(mail)){

            String password = Input.askString("Password: ");

            if (userService.login(mail, password)) {
                System.out.println("LOGIN SUCCESSFUL.");
            } else {
                System.out.println("Incorrect password.");
            }

        } else {
            System.out.println("User not found. Let's create an account");
            register(mail);
        }

    }

    public static void register(String mail) throws Exception {

        UserService userService = new UserService();

        SecurityQuestionDao securityQuestionDao = new SecurityQuestionDao();
        List<SecurityQuestion> securityQuestions = securityQuestionDao.listAll();

        int count = 1;
        for (SecurityQuestion securityQuestion : securityQuestions)
        {
            System.out.println(count + ". " + securityQuestion);
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

        userService.registerUser(user);


    }




}
