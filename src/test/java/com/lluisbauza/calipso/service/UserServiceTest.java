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

    private int securityQuestionId;
    private String mail;
    private String password;
    private String tempPassword;
    private String securityAnswer;
    private String username;
    private String firstName;
    private String lastName1;
    private String lastName2;

    private User tempUser;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        userDao = new UserDao();
        securityQuestionDao = new SecurityQuestionDao();
        userService = new UserService();
        passwordHistoryDao = new PasswordHistoryDao();

        mail = "test@calipso.com";

        SecurityQuestion testSecurityQuestion = new SecurityQuestion("Test question " + System.nanoTime());
        securityQuestionDao.create(testSecurityQuestion);

        List<SecurityQuestion> securityQuestions = securityQuestionDao.listAll();
        securityQuestionId = -1;
        for (SecurityQuestion question : securityQuestions) {
            if (question.getSecurityQuestion().equals(testSecurityQuestion.getSecurityQuestion())) {
                securityQuestionId = question.getIdSecurityQuestion();
            }
        }

        securityAnswer = "Black";
        username = "testBlack";
        firstName = "test";
        lastName1 = "Black";
        lastName2 = "";

        tempUser = new User (securityQuestionId, username, firstName,
                lastName1, lastName2, mail, securityAnswer);

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        User user = userDao.findByMail(mail);

        if (user != null) {
            List<PasswordHistory> histories =
                    passwordHistoryDao.findByUserId(user.getIdUser());

            for (PasswordHistory history : histories) {
                passwordHistoryDao.delete(history.getIdPasswordHistory());
            }

            userDao.delete(user.getIdUser());
        }

        if (securityQuestionId > 0) {
            securityQuestionDao.delete(securityQuestionId);
        }
    }

    @Test
    void registerUser_shouldCreateUserWithTemporaryPassword() throws Exception {

        tempPassword = userService.registerUser(tempUser);

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

    @Test
    void login_shouldReturnTrue_whenCredentialsAreCorrect() throws SQLException, ClassNotFoundException {

        boolean success = false;

        tempPassword = userService.registerUser(tempUser);

        if (userService.userExistsByMail(mail)) {
            success = true;
        }

        assertTrue(success);
        assertTrue(userService.login(mail, tempPassword));
        assertTrue(userService.checkPasswordNeedsChange(mail));

    }

    @Test
    void login_shouldReturnFalse_whenPasswordIsWrong() throws SQLException, ClassNotFoundException {

        boolean success = false;

        tempPassword = userService.registerUser(tempUser);

        if (userService.userExistsByMail(mail)) {
            success = true;
        }

        assertTrue(success);
        assertFalse(userService.login(mail, "testPassword"));

    }

    @Test
    void updatePassword_shouldChangePasswordAndDisableMustChangePassword() throws Exception {

        tempPassword = userService.registerUser(tempUser);

        String newPassword = "helloWorld123";

        assertTrue(userService.isPasswordValid(newPassword));
        assertTrue(userService.isPasswordNew(mail, newPassword));

        userService.updatePassword(mail, newPassword);

        User user = userDao.findByMail(mail);

        assertTrue(BCrypt.checkpw(newPassword, user.getCurrentPasswordHash()));
        assertFalse(user.isMustChangePassword());

    }

}
