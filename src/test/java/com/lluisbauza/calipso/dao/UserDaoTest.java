package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao;
    private int createdId;
    private String testMail;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        userDao = new UserDao();

        User user = new User(1, "psky", "Pancho", "Sky", "",
                "pancho.sky@email.com", "$2a$10$hash6", "Loki");

        userDao.create(user);
        createdId = userDao.findByMail("pancho.sky@email.com").getIdUser();

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException{
        if (userDao.read(createdId) != null)
            userDao.delete(createdId);

        if(testMail != null && userDao.findByMail(testMail) != null) {
            userDao.delete(userDao.findByMail(testMail).getIdUser());
        }

    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException{
        testMail = "ruisu.takeshi@mail.com";

        User user = new User(2, "rtak", "Ruisu", "Takeshi", "",
                testMail, "$2a$10$hash6", "Tokio");
        userDao.create(user);

        User createdUser = userDao.findByMail(testMail);

        assertNotNull(createdUser);
        assertEquals("Ruisu", createdUser.getFirstName());
        assertEquals("rtak", createdUser.getUsername());
    }

    @Test
    void read_shouldReturnUser() throws SQLException, ClassNotFoundException {

        User user = userDao.read(createdId);

        assertNotNull(user);
        assertEquals("pancho.sky@email.com", user.getMail());
        assertEquals("psky", user.getUsername());

    }

    @Test
    void update_shouldModifyData() throws SQLException, ClassNotFoundException{

        User oldUser = userDao.read(createdId);

        oldUser.setIdSecurityQuestion(1);
        oldUser.setUsername("rtak");
        oldUser.setFirstName("Ruisu");
        oldUser.setLastName1("Takeshi");
        oldUser.setLastName2("");
        oldUser.setMail("ruisu.takeshi@mail.com");
        oldUser.setCurrentPasswordHash("$2a$10$hash6");
        oldUser.setSecurityAnswer("Taca");

        userDao.update(oldUser);

        User userUpdated = userDao.read(createdId);

        assertNotNull(userUpdated);
        assertEquals("rtak", userUpdated.getUsername());
        assertEquals("Ruisu", userUpdated.getFirstName());
        assertEquals("Takeshi", userUpdated.getLastName1());
        assertEquals("ruisu.takeshi@mail.com", userUpdated.getMail());

    }

    @Test
    void delete_shouldRemoveUser() throws SQLException, ClassNotFoundException{

        User user = userDao.read(createdId);

        assertNotNull(user);

        userDao.delete(createdId);

        User nullUser = userDao.read(createdId);

        assertNull(nullUser);

    }

    @Test
    void listAll() throws SQLException, ClassNotFoundException{

        List<User> users = userDao.listAll();
        boolean found = false;

        for (User user : users) {
            if (user.getMail().equals("pancho.sky@email.com")) {
                found = true;
                break;
            }

        }

        assertTrue(found);
        assertFalse(users.isEmpty());

    }
}