package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoatDaoTest {

    private BoatDao boatDao;
    private int createdId;
    private String testBoatName;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        boatDao = new BoatDao();
        Boat boat = new Boat("Queen Anne's Revenge", 75);
        boatDao.create(boat);
        createdId = boatDao.findByName("Queen Anne's Revenge").getIdBoat();
    }

    @Test
    void create_shouldInsertBoat() throws SQLException, ClassNotFoundException {

        testBoatName = "Anfitrite";

        Boat boat = new Boat(testBoatName, 18);
        boatDao.create(boat);

        Boat boatNew = boatDao.findByName(testBoatName);

        assertNotNull(boatNew);
        assertEquals(testBoatName, boatNew.getBoatName());
        assertEquals(18, boatNew.getCapacity());

    }

    @Test
    void read_shouldReturnBoat() throws SQLException, ClassNotFoundException {

        Boat boat = boatDao.read(createdId);

        assertNotNull(boat);

        assertEquals(createdId, boat.getIdBoat());
        assertEquals("Queen Anne's Revenge", boat.getBoatName());
        assertEquals(75, boat.getCapacity());

    }

    @Test
    void update_shouldModifyNameAndCapacity() throws SQLException, ClassNotFoundException {

        Boat boat = boatDao.read(createdId);

        boat.setBoatName("Anne's Revenge");
        boat.setCapacity(25);

        boatDao.update(boat);

        boat = boatDao.read(createdId);

        assertNotNull(boat);
        assertEquals("Anne's Revenge", boat.getBoatName());
        assertEquals(25, boat.getCapacity());

    }

    @Test
    void delete_shouldRemoveBoat() throws SQLException, ClassNotFoundException {

        Boat boat = boatDao.read(createdId);

        assertNotNull(boat);

        boatDao.delete(createdId);

        Boat nullBoat = boatDao.read(createdId);

        assertNull(nullBoat);

        createdId = 0;

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<Boat> boats = boatDao.listAll();
        boolean found = false;

        for (Boat boat : boats) {
            if (boat.getBoatName().equals("Queen Anne's Revenge")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(boats.isEmpty());

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (createdId != 0) {
            boatDao.delete(createdId);
        }

        if (testBoatName != null) {
            Boat boat = boatDao.findByName(testBoatName);

            if (boat != null) {
                boatDao.delete(boat.getIdBoat());
            }
        }
    }
}
