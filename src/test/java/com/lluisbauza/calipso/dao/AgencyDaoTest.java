package com.lluisbauza.calipso.dao;
import com.lluisbauza.calipso.model.Agency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyDaoTest {

    private AgencyDao agencyDao;
    private int createdId;
    private String testCif;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        agencyDao = new AgencyDao();
        Agency agency = new Agency("x12345678", "Poseidon", "POX123", 25);
        agencyDao.create(agency);
        createdId = agencyDao.findIdByCif("x12345678");
    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        agencyDao.delete(createdId);

        if (testCif != null)
            agencyDao.delete(agencyDao.findIdByCif(testCif));
    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {

        testCif = "y87654321";

        Agency agency = new Agency(testCif, "Triton", "TRX876", 13);
        agencyDao.create(agency);

        Agency agencyNew = agencyDao.findByCif(testCif);

        assertNotNull(agencyNew);
        int id = agencyDao.findIdByCif(testCif);

        assertEquals(id, agencyNew.getIdAgency());
        assertEquals("Triton", agencyNew.getName());
        assertEquals("TRX876", agencyNew.getAffiliateCode());

    }

    @Test
    void read_shouldReturnAgency() throws SQLException, ClassNotFoundException {

        Agency agency = agencyDao.read(createdId);

        assertNotNull(agency);

        assertEquals(createdId, agency.getIdAgency());
        assertEquals("Poseidon", agency.getName());
        assertEquals("x12345678", agency.getCif());

    }

    @Test
    void update_shouldModifyNameAndDiscountButNotAffiliateCode() throws SQLException, ClassNotFoundException {

        Agency oldAgency = agencyDao.read(createdId);

        String oldAffiliateCode = oldAgency.getAffiliateCode();

        oldAgency.setName("Triton");
        oldAgency.setAffiliateCode("TRX876");
        oldAgency.setDiscount(13);

        agencyDao.update(oldAgency);

        Agency agencyUpdated = agencyDao.read(createdId);

        assertNotNull(agencyUpdated);

        assertEquals("Triton", agencyUpdated.getName());
        assertEquals(13, agencyUpdated.getDiscount());
        assertEquals(oldAffiliateCode, agencyUpdated.getAffiliateCode());

    }

    @Test
    void delete_shouldRemoveAgency() throws SQLException, ClassNotFoundException{

        Agency agency = agencyDao.read(createdId);

        assertNotNull(agency);

        agencyDao.delete(createdId);

        Agency nullAgency = agencyDao.read(createdId);

        assertNull(nullAgency);

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<Agency> agencies = agencyDao.listAll();
        boolean found = false;

        for (Agency agency : agencies) {
            if (agency.getCif().equals("x12345678")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(agencies.isEmpty());

    }

}
