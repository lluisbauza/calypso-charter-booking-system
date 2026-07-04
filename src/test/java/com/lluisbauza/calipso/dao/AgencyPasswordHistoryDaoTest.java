package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.AgencyPasswordHistory;
import com.lluisbauza.calipso.model.AgencyUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyPasswordHistoryDaoTest {

    private AgencyPasswordHistoryDao agencyPasswordHistoryDao;
    private AgencyUserDao agencyUserDao;
    private AgencyDao agencyDao;

    private int createdId, createdAgencyUserId, createdAgencyId;
    private String testPassword;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        agencyPasswordHistoryDao = new AgencyPasswordHistoryDao();
        agencyUserDao = new AgencyUserDao();
        agencyDao = new AgencyDao();

        Agency agency = new Agency("w12345678", "Triton", "TRW123", 20);
        agencyDao.create(agency);
        createdAgencyId = agencyDao.findIdByCif("w12345678");

        AgencyUser agencyUser = new AgencyUser(
                agencyDao.read(createdAgencyId),
                "Luca Bianchi",
                "luca.bianchi@testagency.com",
                "$2a$10$hashLuca",
                true
        );

        agencyUserDao.create(agencyUser);
        createdAgencyUserId = agencyUserDao.findIdByMail("luca.bianchi@testagency.com");

        AgencyPasswordHistory agencyPasswordHistory = new AgencyPasswordHistory(
                agencyUserDao.read(createdAgencyUserId),
                "agencyPasswordTest1234"
        );

        agencyPasswordHistoryDao.create(agencyPasswordHistory);
        createdId = agencyPasswordHistoryDao.findIdByPassword("agencyPasswordTest1234");
    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (agencyPasswordHistoryDao.read(createdId) != null) {
            agencyPasswordHistoryDao.delete(createdId);
        }

        if (testPassword != null && agencyPasswordHistoryDao.findByPassword(testPassword) != null) {
            agencyPasswordHistoryDao.delete(
                    agencyPasswordHistoryDao.findByPassword(testPassword).getIdAgencyPasswordHistory()
            );
        }

        if (agencyUserDao.read(createdAgencyUserId) != null) {
            agencyUserDao.delete(createdAgencyUserId);
        }

        if (agencyDao.read(createdAgencyId) != null) {
            agencyDao.delete(createdAgencyId);
        }
    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {
        testPassword = "agencyPassword5678";

        AgencyPasswordHistory agencyPasswordHistory = new AgencyPasswordHistory(
                agencyUserDao.read(createdAgencyUserId),
                testPassword
        );

        agencyPasswordHistoryDao.create(agencyPasswordHistory);

        AgencyPasswordHistory newAgencyPasswordHistory = agencyPasswordHistoryDao.findByPassword(testPassword);

        assertNotNull(newAgencyPasswordHistory);
        assertEquals(testPassword, newAgencyPasswordHistory.getPasswordHash());
        assertEquals(createdAgencyUserId, newAgencyPasswordHistory.getAgencyUser().getIdAgencyUser());
    }

    @Test
    void read_shouldReturnAgencyPasswordHistory() throws SQLException, ClassNotFoundException {
        AgencyPasswordHistory agencyPasswordHistory = agencyPasswordHistoryDao.read(createdId);

        assertNotNull(agencyPasswordHistory);
        assertEquals(createdId, agencyPasswordHistory.getIdAgencyPasswordHistory());
        assertEquals("agencyPasswordTest1234", agencyPasswordHistory.getPasswordHash());
        assertEquals(createdAgencyUserId, agencyPasswordHistory.getAgencyUser().getIdAgencyUser());
    }

    @Test
    void update_shouldModifyAgencyPasswordHistory() throws SQLException, ClassNotFoundException {
        AgencyPasswordHistory oldAgencyPasswordHistory = agencyPasswordHistoryDao.read(createdId);

        oldAgencyPasswordHistory.setPasswordHash("agencyPasswordUpdated");

        agencyPasswordHistoryDao.update(oldAgencyPasswordHistory);

        AgencyPasswordHistory agencyPasswordHistoryUpdated = agencyPasswordHistoryDao.read(createdId);

        assertNotNull(agencyPasswordHistoryUpdated);
        assertEquals("agencyPasswordUpdated", agencyPasswordHistoryUpdated.getPasswordHash());
        assertEquals(createdAgencyUserId, agencyPasswordHistoryUpdated.getAgencyUser().getIdAgencyUser());
    }

    @Test
    void delete_shouldRemoveAgencyPasswordHistory() throws SQLException, ClassNotFoundException {
        AgencyPasswordHistory agencyPasswordHistory = agencyPasswordHistoryDao.read(createdId);

        assertNotNull(agencyPasswordHistory);

        agencyPasswordHistoryDao.delete(createdId);

        AgencyPasswordHistory nullAgencyPasswordHistory = agencyPasswordHistoryDao.read(createdId);

        assertNull(nullAgencyPasswordHistory);
    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<AgencyPasswordHistory> agencyPasswordHistories = agencyPasswordHistoryDao.listAll();
        boolean found = false;

        for (AgencyPasswordHistory agencyPasswordHistory : agencyPasswordHistories) {
            if (agencyPasswordHistory.getPasswordHash().equals("agencyPasswordTest1234")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(agencyPasswordHistories.isEmpty());
    }

    @Test
    void findByAgencyUserId_shouldReturnAgencyPasswordHistories() throws SQLException, ClassNotFoundException {
        List<AgencyPasswordHistory> agencyPasswordHistories =
                agencyPasswordHistoryDao.findByAgencyUserId(createdAgencyUserId);

        boolean found = false;

        for (AgencyPasswordHistory agencyPasswordHistory : agencyPasswordHistories) {
            if (agencyPasswordHistory.getPasswordHash().equals("agencyPasswordTest1234")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(agencyPasswordHistories.isEmpty());
    }

}