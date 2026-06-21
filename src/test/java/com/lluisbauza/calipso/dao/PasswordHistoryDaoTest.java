package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordHistoryDaoTest {

    private PasswordHistoryDao passwordHistoryDao;
    private UserDao userDao;
    private int createdId, createdUserId;
    private String testPassword;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        passwordHistoryDao = new PasswordHistoryDao();
        userDao = new UserDao();
        User testUser = new User(1, "psky", "Pancho", "Sky", "",
                "pancho.sky@email.com", true, "$2a$10$hash6", "Loki");
        userDao.create(testUser);

        User user = userDao.findByMail(testUser.getMail());
        createdUserId = user.getIdUser();

        PasswordHistory passwordHistory = new PasswordHistory(user, "passwordTest1234");
        passwordHistoryDao.create(passwordHistory);
        createdId = passwordHistoryDao.findIdByPassword("passwordTest1234");
    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {

        testPassword = "12345678";

        PasswordHistory passwordHistory = new PasswordHistory(userDao.read(createdUserId), "12345678");
        passwordHistoryDao.create(passwordHistory);

        PasswordHistory newPasswordHistory = passwordHistoryDao.findByPassword(testPassword);

        assertNotNull(newPasswordHistory);
        int id = passwordHistoryDao.findIdByPassword(testPassword);

        assertEquals(id, newPasswordHistory.getIdPasswordHistory());
        assertEquals("12345678", newPasswordHistory.getPasswordHash());
        assertEquals(createdUserId, newPasswordHistory.getUser().getIdUser());

    }

    @Test
    void read_shouldReturnPasswordHistory() throws SQLException, ClassNotFoundException {

        PasswordHistory passwordHistory = passwordHistoryDao.read(createdId);

        assertNotNull(passwordHistory);

        assertEquals(createdId, passwordHistory.getIdPasswordHistory());
        assertEquals("passwordTest1234", passwordHistory.getPasswordHash());
        assertEquals(createdUserId, passwordHistory.getUser().getIdUser());

    }

    @Test
    void update_shouldModifyPasswordHistory() throws SQLException, ClassNotFoundException {

        PasswordHistory oldPasswordHistory = passwordHistoryDao.read(createdId);

        String oldPassword = oldPasswordHistory.getPasswordHash();

        oldPasswordHistory.setPasswordHash("6789poert");

        passwordHistoryDao.update(oldPasswordHistory);

        PasswordHistory passwordHistoryUpdated = passwordHistoryDao.read(createdId);

        assertNotNull(passwordHistoryUpdated);

        assertEquals("6789poert", passwordHistoryUpdated.getPasswordHash());
        assertEquals(createdUserId, passwordHistoryUpdated.getUser().getIdUser());

    }

    @Test
    void delete_shouldRemovePasswordHistory() throws SQLException, ClassNotFoundException{

        PasswordHistory passwordHistory = passwordHistoryDao.read(createdId);

        assertNotNull(passwordHistory);

        passwordHistoryDao.delete(createdId);

        PasswordHistory nullPasswordHistory = passwordHistoryDao.read(createdId);

        assertNull(nullPasswordHistory);

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {

        List<PasswordHistory> passwordHistoryList = passwordHistoryDao.listAll();
        boolean found = false;

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordHistory.getPasswordHash().equals("passwordTest1234")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(passwordHistoryList.isEmpty());

    }

    @Test
    void findByUserId() throws SQLException, ClassNotFoundException {

        List<PasswordHistory> userPasswordList = passwordHistoryDao.findByUserId(createdUserId);
        boolean found = false;

        for (PasswordHistory passwordHistory : userPasswordList) {
            if (passwordHistory.getPasswordHash().equals("passwordTest1234")) {
                found = true;
                break;
            }
        }

        assertNotNull(userDao.read(createdUserId));
        assertTrue(found);
        assertFalse(userPasswordList.isEmpty());

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (passwordHistoryDao.read(createdId) != null) {
            passwordHistoryDao.delete(createdId);
        }

        if (testPassword != null) {
            PasswordHistory testHistory = passwordHistoryDao.findByPassword(testPassword);

            if (testHistory != null) {
                passwordHistoryDao.delete(testHistory.getIdPasswordHistory());
            }
        }

        if (userDao.read(createdUserId) != null) {
            userDao.delete(createdUserId);
        }

    }

}
