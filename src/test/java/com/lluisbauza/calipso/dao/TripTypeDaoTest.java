package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.TripType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TripTypeDaoTest {
    
    private TripTypeDao tripTypeDao;
    private BoatDao boatDao;
    private int createdId, createdBoatId;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{

        tripTypeDao = new TripTypeDao();
        boatDao = new BoatDao();

        Boat testBoat = new Boat("Queen Anne's Revenge", 75);
        boatDao.create(testBoat);

        Boat boat = boatDao.findByName("Queen Anne's Revenge");
        createdBoatId = boat.getIdBoat();

        TripType tripType = new TripType(boat, "Sunset", Duration.ofHours(2),
                LocalTime.of(19, 0), 109.99);

        tripTypeDao.create(tripType);

        List<TripType> tripTypes = tripTypeDao.findByBoatIdAndOption(createdBoatId, "Sunset");
        for (TripType type : tripTypes) {
            if (type.getDepartureTime().equals(tripType.getDepartureTime())) {
                createdId = type.getIdTripType();
            }
        }

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException{
        if (tripTypeDao.read(createdId) != null)
            tripTypeDao.delete(createdId);

        if (boatDao.read(createdBoatId) != null) {
            boatDao.delete(createdBoatId);
        }
    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException{

        int id = 0;

        Boat existingBoat = boatDao.findByName("Anfitrite");

        if (existingBoat != null) {
            List<TripType> existingTypes = tripTypeDao.findByBoatId(existingBoat.getIdBoat());

            for (TripType type : existingTypes) {
                tripTypeDao.delete(type.getIdTripType());
            }

            boatDao.delete(existingBoat.getIdBoat());
        }

        Boat boat = new Boat("Anfitrite", 18);
        boatDao.create(boat);

        Boat updateBoat = boatDao.findByName("Anfitrite");

        TripType tripType = new TripType(updateBoat, "Morning", Duration.ofHours(2),
                LocalTime.of(10, 0), 69.99);
        tripTypeDao.create(tripType);

        List<TripType> tripTypes = tripTypeDao.findByBoatIdAndOption(updateBoat.getIdBoat(), "Morning");
        for (TripType type : tripTypes) {
            if (type.getDepartureTime().equals(tripType.getDepartureTime())) {
                id = type.getIdTripType();
            }
        }

        assertNotEquals(0, id);

        TripType createdTripType = tripTypeDao.read(id);

        assertNotNull(createdTripType);
        assertEquals("Anfitrite", createdTripType.getBoat().getBoatName());
        assertEquals("Morning", createdTripType.getTripOption());
        assertEquals(69.99, createdTripType.getPrice());

        tripTypeDao.delete(id);
        boatDao.delete(updateBoat.getIdBoat());

    }

    @Test
    void read_shouldReturnTripType() throws SQLException, ClassNotFoundException {

        TripType tripType = tripTypeDao.read(createdId);

        assertNotNull(tripType);
        assertEquals("Queen Anne's Revenge", tripType.getBoat().getBoatName());
        assertEquals("Sunset", tripType.getTripOption());
        assertEquals(109.99, tripType.getPrice());

    }

    @Test
    void update_shouldModifyData() throws SQLException, ClassNotFoundException{

        TripType oldTripType = tripTypeDao.read(createdId);

        oldTripType.setTripOption("Morning");
        oldTripType.setDuration(Duration.ofHours(4));
        oldTripType.setDepartureTime(LocalTime.of(10, 0));
        oldTripType.setPrice(99.99);

        tripTypeDao.update(oldTripType);

        TripType tripTypeUpdated = tripTypeDao.read(createdId);

        assertNotNull(tripTypeUpdated);
        assertEquals("Morning", tripTypeUpdated.getTripOption());
        assertEquals(Duration.ofHours(4), tripTypeUpdated.getDuration());
        assertEquals(LocalTime.of(10, 0), tripTypeUpdated.getDepartureTime());
        assertEquals(99.99, tripTypeUpdated.getPrice());

    }

    @Test
    void delete_shouldRemoveTripType() throws SQLException, ClassNotFoundException{

        TripType tripType = tripTypeDao.read(createdId);

        assertNotNull(tripType);

        tripTypeDao.delete(createdId);

        TripType nullTripType = tripTypeDao.read(createdId);

        assertNull(nullTripType);

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException{

        List<TripType> tripTypes = tripTypeDao.listAll();
        boolean found = false;

        for (TripType tripType : tripTypes) {
            if (tripType.getTripOption().equals("Sunset") && tripType.getBoat().getBoatName().equals("Queen Anne's Revenge")
            && tripType.getDepartureTime().equals(LocalTime.of(19, 0))) {
                found = true;
                break;
            }

        }

        assertTrue(found);
        assertFalse(tripTypes.isEmpty());

    }

}
