package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.PasswordHistoryDao;
import com.lluisbauza.calipso.dao.SecurityQuestionDao;
import com.lluisbauza.calipso.dao.UserDao;
import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.model.User;

import java.sql.SQLException;
import java.util.List;

import com.lluisbauza.calipso.util.PasswordGenerator;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private UserDao userDao = new UserDao();
    private PasswordHistoryDao passwordHistoryDao = new PasswordHistoryDao();

    public UserService() throws SQLException, ClassNotFoundException {
    }

    public boolean userExistsByMail(String mail) throws SQLException, ClassNotFoundException {

        return userDao.findByMail(mail) != null;

    }

    public boolean login(String mail, String password) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return (user != null && BCrypt.checkpw(password, user.getCurrentPasswordHash()));

    }

    public String registerUser(User user) throws SQLException, ClassNotFoundException {

        String password = PasswordGenerator.generateTempPassword();
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setCurrentPasswordHash(passwordHash);

        userDao.create(user);

        PasswordHistory passwordHistory = new PasswordHistory(user, passwordHash);

        passwordHistoryDao.create(passwordHistory);

        return password;
    }

    public String getQuestionByMail(String mail) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        SecurityQuestionDao securityQuestionDao = new SecurityQuestionDao();

        String question = (securityQuestionDao.read(user.getIdSecurityQuestion())).getSecurityQuestion();

        return question;
    }

    public boolean checkPasswordNeedsChange(String mail) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return user.isMustChangePassword();

    }

    public boolean confirmAnswer(String mail, String answer) throws  SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        return user.getSecurityAnswer().equals(answer);
    }

    public void updatePassword(String mail, String password) throws SQLException, ClassNotFoundException {

        User user = userDao.findByMail(mail);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setCurrentPasswordHash(hashedPassword);
        user.setMustChangePassword(false);

        userDao.updatePassword(user);

        PasswordHistory passwordHistory = new PasswordHistory(user, hashedPassword);
        passwordHistoryDao.create(passwordHistory);

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

}
