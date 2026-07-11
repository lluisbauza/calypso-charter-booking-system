package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.PasswordHistoryDao;
import com.lluisbauza.calipso.dao.SecurityQuestionDao;
import com.lluisbauza.calipso.dao.UserDao;
import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lluisbauza.calipso.util.PasswordFileGenerator;
import com.lluisbauza.calipso.util.PasswordGenerator;
import org.mindrot.jbcrypt.BCrypt;

import static java.lang.Character.*;

public class UserService {

    private UserDao userDao = new UserDao();
    private PasswordHistoryDao passwordHistoryDao = new PasswordHistoryDao();
    private SecurityQuestionDao securityQuestionDao = new SecurityQuestionDao();


    public UserService() throws SQLException, ClassNotFoundException {
    }

    // AUTHENTICATION
    public boolean userExistsByMail(String mail) throws SQLException, ClassNotFoundException {

        return userDao.findByMail(mail) != null;

    }

    public boolean login(String mail, String password) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return (user != null && BCrypt.checkpw(password, user.getCurrentPasswordHash()));

    }

    public boolean checkPasswordNeedsChange(String mail) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return user.isMustChangePassword();

    }

    // PASSWORD MANAGEMENT
    public boolean isPasswordValid(String password) throws Exception {

        boolean longEnough = false;
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasNumber = false;

        if (password.length() >= 10) {
            longEnough = true;
        }

        for (int i = 0, length = password.length(); i < length; i++) {

            if (!hasLowerCase)
                hasLowerCase = isLowerCase(password.charAt(i));

            if (!hasUpperCase)
                hasUpperCase = isUpperCase(password.charAt(i));

            if (!hasNumber)
                hasNumber = isDigit(password.charAt(i));

        }

        if (!longEnough)
            throw new Exception("The password must have at least 10 characters.");

        if (!hasLowerCase)
            throw new Exception("The password must have at least an lowercase letter");

        if (!hasUpperCase)
            throw new Exception("The password must have at least an uppercase letter");

        if (!hasNumber)
            throw new Exception("The password must have at least a number");

        return longEnough && hasLowerCase && hasUpperCase && hasNumber;

    }

    public boolean isPasswordNew(String mail, String newPassword) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        List<PasswordHistory> passwordHistories = passwordHistoryDao.findByUserId(user.getIdUser());

        for (PasswordHistory passwordHistory : passwordHistories) {

            if (BCrypt.checkpw(newPassword, passwordHistory.getPasswordHash()))
            {
                return false;
            }

        }

        return true;

    }

    public void updatePassword(String mail, String password) throws Exception {

        isPasswordValid(password);

        if (!isPasswordNew(mail, password)) {
            throw new IllegalArgumentException(
                    "You can't use a password you've used in the past."
            );
        }

        User user = userDao.findByMail(mail);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setCurrentPasswordHash(hashedPassword);
        user.setMustChangePassword(false);

        userDao.updatePassword(user);

        PasswordHistory passwordHistory = new PasswordHistory(user, hashedPassword);
        passwordHistoryDao.create(passwordHistory);

    }

    // PASSWORD RECOVERY

    public String getQuestionByMail(String mail) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        SecurityQuestionDao securityQuestionDao = new SecurityQuestionDao();

        String question = (securityQuestionDao.read(user.getIdSecurityQuestion())).getSecurityQuestion();

        return question;
    }

    public boolean confirmAnswer(String mail, String answer) throws  SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return user.getSecurityAnswer().equals(answer);
    }

    // USER REGISTRATION

    public List<SecurityQuestion> getSecurityQuestions()
            throws SQLException, ClassNotFoundException {
        return securityQuestionDao.listAll();
    }

    public String registerUser(User user) throws SQLException, ClassNotFoundException {

        String password = PasswordGenerator.generateTempPassword();
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setCurrentPasswordHash(passwordHash);

        userDao.create(user);

        PasswordHistory passwordHistory = new PasswordHistory(user, passwordHash);

        passwordHistoryDao.create(passwordHistory);

        try {
            PasswordFileGenerator.generatePasswordFile(user, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return password;
    }


}
