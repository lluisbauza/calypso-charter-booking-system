package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.PasswordHistoryDao;
import com.lluisbauza.calipso.dao.UserDao;
import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.model.User;

import java.security.SecureRandom;
import java.sql.SQLException;

public class UserService {

    private UserDao userDao = new UserDao();
    private PasswordHistoryDao passwordHistoryDao = new PasswordHistoryDao();

    public UserService() throws SQLException, ClassNotFoundException {
    }

    public boolean userExistsByMail(String mail) throws SQLException, ClassNotFoundException {

        if (userDao.findByMail(mail) != null) {
            return true;
        }

        return false;
    }

    public boolean login(String mail, String password) throws SQLException, ClassNotFoundException {


        User user = userDao.findByMail(mail);

        if (user != null && user.getCurrentPasswordHash().equals(password)) {
            return true;
        }

        return false;
    }

    public void registerUser(User user) throws SQLException, ClassNotFoundException {

        String password = generateTempPassword();

        user.setCurrentPasswordHash(password);

        userDao.create(user);

        PasswordHistory passwordHistory = new PasswordHistory(user, password);

        passwordHistoryDao.create(passwordHistory);

    }

    private String generateTempPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

}
