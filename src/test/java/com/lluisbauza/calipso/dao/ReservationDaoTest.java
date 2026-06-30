package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationDaoTest {

    private ReservationDao reservationDao;
    private AgencyDao agencyDao;
    private ClientDao clientDao;
    private BoatDao boatDao;
    private TripTypeDao tripTypeDao;

    private int createdId, idAgency, idClient, idBoat, idTripType;

    private String reservationCode;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {

        reservationDao = new ReservationDao();
        agencyDao = new AgencyDao();
        clientDao = new ClientDao();
        boatDao = new BoatDao();
        tripTypeDao = new TripTypeDao();

        Agency baseAgency = new Agency("y12345678", "Poseidon", "POX123", 25);
        agencyDao.create(baseAgency);
        Agency agency = agencyDao.findByCif("y12345678");
        idAgency = agency.getIdAgency();

        Client baseClient = new Client("client@calipso.com", "+34666777888", "Admin");
        clientDao.create(baseClient);
        Client client = clientDao.findByMail("client@calipso.com");
        idClient = client.getIdClient();

        Boat baseBoat = new Boat("Blue Water High", 25);
        boatDao.create(baseBoat);
        Boat boat = boatDao.findByName("Blue Water High");
        idBoat = boat.getIdBoat();

        TripType baseTripType = new TripType(boat, "Sunset", Duration.ofHours(2),
                LocalTime.of(19, 0), 109.99);
        tripTypeDao.create(baseTripType);

        List<TripType> tripTypes = tripTypeDao.findByBoatIdAndOption(idBoat, "Sunset");
        for (TripType type : tripTypes) {
            if (type.getDepartureTime().equals(baseTripType.getDepartureTime())) {
                idTripType = type.getIdTripType();
            }
        }

        assertNotEquals(0, idTripType);

        TripType tripType = tripTypeDao.read(idTripType);

        Reservation reservation = new Reservation(10, "1234ABCD", "", client, tripType,
                LocalDate.of(2026, 8, 15), false,
                399.99, agency, ReservationStatus.PENDING);

        reservationDao.create(reservation);
        createdId = reservationDao.findByReservationCode("1234ABCD").getIdReservation();
    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (reservationDao.read(createdId) != null)
            reservationDao.delete(createdId);

        if (reservationCode != null) {
            Reservation reservation = reservationDao.findByReservationCode(reservationCode);
            if (reservation != null) {
                reservationDao.delete(reservation.getIdReservation());
            }
        }

        if (tripTypeDao.read(idTripType) != null)
            tripTypeDao.delete(idTripType);

        if (boatDao.read(idBoat) != null)
            boatDao.delete(idBoat);

        if (clientDao.read(idClient) != null)
            clientDao.delete(idClient);

        if (agencyDao.read(idAgency) != null)
            agencyDao.delete(idAgency);

    }

    @Test
    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {

        reservationCode = "ABCD1234";

        Reservation reservation = new Reservation(15, reservationCode, "",
                clientDao.read(idClient), tripTypeDao.read(idTripType), LocalDate.of(2026, 10, 15),
                true, 369.99, null, ReservationStatus.COMPLETED);

        reservationDao.create(reservation);

        Reservation reservationNew = reservationDao.findByReservationCode(reservationCode);

        assertNotNull(reservationNew);

        assertTrue(reservationNew.getIdReservation() > 0);
        assertEquals(15, reservationNew.getPax());
        assertTrue(reservationNew.isAllergies());
        assertEquals(369.99, reservationNew.getFinalPrice(), 0.01);
        assertEquals(ReservationStatus.COMPLETED, reservationNew.getReservationStatus());

    }

    @Test
    void read_shouldReturnReservation() throws SQLException, ClassNotFoundException {

        Reservation reservation = reservationDao.read(createdId);

        assertNotNull(reservation);

        assertEquals(createdId, reservation.getIdReservation());
        assertEquals(10, reservation.getPax());
        assertEquals("1234ABCD", reservation.getReservationCode());
        assertEquals(LocalDate.of(2026, 8, 15), reservation.getReservationDate());
        assertFalse(reservation.isAllergies());
        assertEquals(399.99, reservation.getFinalPrice(), 0.01);
        assertEquals("", reservation.getObservations());
        assertEquals(ReservationStatus.PENDING, reservation.getReservationStatus());
        assertEquals("Sunset", reservation.getTripType().getTripOption());
        assertEquals("client@calipso.com", reservation.getClient().getMail());

    }

    @Test
    void update_shouldModifyData() throws SQLException, ClassNotFoundException{

        Reservation oldReservation = reservationDao.read(createdId);

        oldReservation.setPax(15);
        oldReservation.setReservationDate(LocalDate.of(2026, 10, 15));
        oldReservation.setAllergies(true);
        oldReservation.setFinalPrice(99.99);
        oldReservation.setObservations("Peanuts");
        oldReservation.setReservationStatus(ReservationStatus.REFUNDED);

        reservationDao.update(oldReservation);

        Reservation reservationUpdated = reservationDao.read(createdId);

        assertNotNull(reservationUpdated);
        assertEquals(15, reservationUpdated.getPax());
        assertEquals(LocalDate.of(2026, 10, 15), reservationUpdated.getReservationDate());
        assertTrue(reservationUpdated.isAllergies());
        assertEquals(99.99, reservationUpdated.getFinalPrice(), 0.01);
        assertEquals("Peanuts", reservationUpdated.getObservations());
        assertEquals(ReservationStatus.REFUNDED, reservationUpdated.getReservationStatus());
    }

    @Test
    void delete_shouldRemoveReservation() throws SQLException, ClassNotFoundException{

        Reservation reservation = reservationDao.read(createdId);

        assertNotNull(reservation);

        reservationDao.delete(createdId);

        Reservation nullReservation = reservationDao.read(createdId);

        assertNull(nullReservation);

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<Reservation> reservations = reservationDao.listAll();
        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getReservationCode().equals("1234ABCD")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(reservations.isEmpty());

    }

    @Test
    void findByReservationCode_shouldReturnReservation() throws SQLException, ClassNotFoundException {

        Reservation reservation = reservationDao.findByReservationCode("1234ABCD");

        assertNotNull(reservation);
        assertEquals(createdId, reservation.getIdReservation());
        assertEquals("1234ABCD", reservation.getReservationCode());

    }

    @Test
    void findByClientId_shouldReturnReservations() throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = reservationDao.findByClientId(idClient);
        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getIdReservation() == createdId) {
                found = true;
                break;
            }
        }

        assertFalse(reservations.isEmpty());
        assertTrue(found);

    }

    @Test
    void findByDate_shouldReturnReservations() throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = reservationDao.findByDate(LocalDate.of(2026, 8, 15));
        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getReservationCode().equals("1234ABCD")) {
                found = true;
                break;
            }
        }

        assertFalse(reservations.isEmpty());
        assertTrue(found);

    }

    @Test
    void findByTripTypeAndDate_shouldReturnReservations() throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = reservationDao.findByTripTypeAndDate(
                idTripType,
                LocalDate.of(2026, 8, 15)
        );

        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getIdReservation() == createdId) {
                found = true;
                break;
            }
        }

        assertFalse(reservations.isEmpty());
        assertTrue(found);

    }

    @Test
    void findByStatus_shouldReturnReservations() throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = reservationDao.findByStatus(ReservationStatus.PENDING);
        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getReservationCode().equals("1234ABCD")) {
                found = true;
                break;
            }
        }

        assertFalse(reservations.isEmpty());
        assertTrue(found);

    }

    @Test
    void findByAgencyId_shouldReturnReservations() throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = reservationDao.findByAgencyId(idAgency);
        boolean found = false;

        for (Reservation reservation : reservations) {
            if (reservation.getIdReservation() == createdId) {
                found = true;
                break;
            }
        }

        assertFalse(reservations.isEmpty());
        assertTrue(found);

    }

    @Test
    void countReservedSeats_shouldReturnTotalPax() throws SQLException, ClassNotFoundException {

        int reservedSeats = reservationDao.countReservedSeats(
                idTripType,
                LocalDate.of(2026, 8, 15)
        );

        assertEquals(10, reservedSeats);

    }

}