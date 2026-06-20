package com.lluisbauza.calipso.dao;
import com.lluisbauza.calipso.model.Agency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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
    void update_shouldModifyInfo() throws SQLException, ClassNotFoundException {

        Agency agency = new Agency("x12345678", "Triton", "TRX876", 13);

        agencyDao.update(agency);

        Agency agencyUpdated = agencyDao.read(createdId);

        assertEquals("Triton", agencyUpdated.getName());
        assertEquals(13, agencyUpdated.getDiscount());

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        agencyDao.delete(createdId);

        if (testCif != null)
            agencyDao.delete(agencyDao.findIdByCif(testCif));
    }

}
