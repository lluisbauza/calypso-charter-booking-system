package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.PasswordHistoryDao;
import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.dao.SecurityQuestionDao;
import com.lluisbauza.calipso.dao.UserDao;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserDao userDao;
    private SecurityQuestionDao securityQuestionDao;
    private UserService userService;
    private PasswordHistoryDao passwordHistoryDao;
    private String mail;
    private int securityQuestionId;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        userDao = new UserDao();
        securityQuestionDao = new SecurityQuestionDao();
        userService = new UserService();
        passwordHistoryDao = new PasswordHistoryDao();

        mail = "lluis@calipso.com";

        SecurityQuestion testSecurityQuestion = new SecurityQuestion("Test question " + System.nanoTime());
        securityQuestionDao.create(testSecurityQuestion);

        List<SecurityQuestion> securityQuestions = securityQuestionDao.listAll();
        securityQuestionId = -1;
        for (SecurityQuestion question : securityQuestions) {
            if (question.getSecurityQuestion().equals(testSecurityQuestion.getSecurityQuestion())) {
                securityQuestionId = question.getIdSecurityQuestion();
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        User user = userDao.findByMail(mail);

        if (user != null) {
            PasswordHistory passwordHistory =
                    passwordHistoryDao.findByPassword(user.getCurrentPasswordHash());

            if (passwordHistory != null) {
                passwordHistoryDao.delete(passwordHistory.getIdPasswordHistory());
            }

            userDao.delete(user.getIdUser());
        }

        if (securityQuestionId > 0) {
            securityQuestionDao.delete(securityQuestionId);
        }
    }

    @Test
    void registerUser_shouldCreateUserWithTemporaryPassword() throws Exception {

        String securityAnswer = "Black";
        String username = "lluisBlack";
        String firstName = "Lluis";
        String lastName1 = "Bauzá";
        String lastName2 = "";

        User tempUser = new User (securityQuestionId, username, firstName,
                lastName1, lastName2, mail, securityAnswer);

        String tempPassword = userService.registerUser(tempUser);

        User user = userDao.findByMail(mail);

        assertNotNull(tempPassword);
        assertNotNull(user);
        assertNotNull(userDao.read(user.getIdUser()));
        assertTrue(user.getIdUser() > 0);
        assertFalse(user.getCurrentPasswordHash().equals(tempPassword));
        assertTrue(BCrypt.checkpw(tempPassword, user.getCurrentPasswordHash()));
        assertNotNull(passwordHistoryDao.findByPassword(user.getCurrentPasswordHash()));
        assertTrue(user.isMustChangePassword());
    }


}
