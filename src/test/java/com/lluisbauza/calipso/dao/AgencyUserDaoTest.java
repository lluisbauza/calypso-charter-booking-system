package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.AgencyUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyUserDaoTest {

    private AgencyUserDao agencyUserDao;
    private AgencyDao agencyDao;
    private int createdId, createdAgencyId;
    private String testMail;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        agencyUserDao = new AgencyUserDao();
        agencyDao = new AgencyDao();

        Agency agency = new Agency("z12345678", "Nereus", "NEZ123", 15);
        agencyDao.create(agency);
        createdAgencyId = agencyDao.findIdByCif("z12345678");

        AgencyUser agencyUser = new AgencyUser(
                agencyDao.read(createdAgencyId),
                "Thomas Weber",
                "thomas.weber@testagency.com",
                "$2a$10$hashThomas",
                true
        );

        agencyUserDao.create(agencyUser);
        createdId = agencyUserDao.findIdByMail("thomas.weber@testagency.com");
    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {
        testMail = "claire.martin@testagency.com";

        AgencyUser agencyUser = new AgencyUser(
                agencyDao.read(createdAgencyId),
                "Claire Martin",
                testMail,
                "$2a$10$hashClaire",
                true
        );

        agencyUserDao.create(agencyUser);

        AgencyUser createdAgencyUser = agencyUserDao.findByMail(testMail);

        assertNotNull(createdAgencyUser);
        assertEquals("Claire Martin", createdAgencyUser.getName());
        assertEquals(testMail, createdAgencyUser.getMail());
        assertEquals(createdAgencyId, createdAgencyUser.getAgency().getIdAgency());
        assertTrue(createdAgencyUser.isActive());
    }

    @Test
    void read_shouldReturnAgencyUser() throws SQLException, ClassNotFoundException {
        AgencyUser agencyUser = agencyUserDao.read(createdId);

        assertNotNull(agencyUser);
        assertEquals(createdId, agencyUser.getIdAgencyUser());
        assertEquals("Thomas Weber", agencyUser.getName());
        assertEquals("thomas.weber@testagency.com", agencyUser.getMail());
        assertEquals(createdAgencyId, agencyUser.getAgency().getIdAgency());
    }

    @Test
    void update_shouldModifyAgencyUser() throws SQLException, ClassNotFoundException {
        AgencyUser oldAgencyUser = agencyUserDao.read(createdId);

        oldAgencyUser.setName("Thomas Müller");
        oldAgencyUser.setMail("thomas.mueller@testagency.com");
        oldAgencyUser.setPasswordHash("$2a$10$hashUpdated");
        oldAgencyUser.setActive(false);

        agencyUserDao.update(oldAgencyUser);

        AgencyUser agencyUserUpdated = agencyUserDao.read(createdId);

        assertNotNull(agencyUserUpdated);
        assertEquals("Thomas Müller", agencyUserUpdated.getName());
        assertEquals("thomas.mueller@testagency.com", agencyUserUpdated.getMail());
        assertEquals("$2a$10$hashUpdated", agencyUserUpdated.getPasswordHash());
        assertFalse(agencyUserUpdated.isActive());
    }

    @Test
    void delete_shouldRemoveAgencyUser() throws SQLException, ClassNotFoundException {
        AgencyUser agencyUser = agencyUserDao.read(createdId);

        assertNotNull(agencyUser);

        agencyUserDao.delete(createdId);

        AgencyUser nullAgencyUser = agencyUserDao.read(createdId);

        assertNull(nullAgencyUser);
    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<AgencyUser> agencyUsers = agencyUserDao.listAll();
        boolean found = false;

        for (AgencyUser agencyUser : agencyUsers) {
            if (agencyUser.getMail().equals("thomas.weber@testagency.com")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(agencyUsers.isEmpty());
    }

    @Test
    void findByAgencyId_shouldReturnAgencyUsers() throws SQLException, ClassNotFoundException {
        List<AgencyUser> agencyUsers = agencyUserDao.findByAgencyId(createdAgencyId);
        boolean found = false;

        for (AgencyUser agencyUser : agencyUsers) {
            if (agencyUser.getMail().equals("thomas.weber@testagency.com")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(agencyUsers.isEmpty());
    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (agencyUserDao.read(createdId) != null) {
            agencyUserDao.delete(createdId);
        }

        if (testMail != null && agencyUserDao.findByMail(testMail) != null) {
            agencyUserDao.delete(agencyUserDao.findByMail(testMail).getIdAgencyUser());
        }

        if (agencyDao.read(createdAgencyId) != null) {
            agencyDao.delete(createdAgencyId);
        }
    }
}