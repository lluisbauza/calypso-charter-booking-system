package com.lluisbauza.calipso.dao;
import com.lluisbauza.calipso.model.Agency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyDaoTest {

    private AgencyDao dao;
    private int createdId;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        dao = new AgencyDao();
        Agency agency = new Agency("x12345678", "Poseidon", "POX123", 25);
        dao.create(agency);
        createdId = dao.findIdByCif("x12345678");
    }

    @Test
    void read_shouldReturnAgency() throws SQLException, ClassNotFoundException {

        Agency agency = dao.read(createdId);

        assertNotNull(agency);

        assertEquals(createdId, agency.getIdAgency());
        assertEquals("Poseidon", agency.getName());
        assertEquals("x12345678", agency.getCif());

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        dao.delete(createdId);
    }

}
