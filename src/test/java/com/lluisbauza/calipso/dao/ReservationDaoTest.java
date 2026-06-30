package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.Client;
import com.lluisbauza.calipso.model.TripType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class ReservationDaoTest {

    private ReservationDao reservationDao;
    private AgencyDao agencyDao;
    private ClientDao clientDao;
    private BoatDao boatDao;
    private TripTypeDao tripTypeDao;

    private int idAgency;
    private int idClient;
    private int idBoat;
    private int idTripType;

    private String reservationCode;


    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{

        reservationDao = new ReservationDao();
        agencyDao = new AgencyDao();
        clientDao = new ClientDao();
        boatDao = new BoatDao();
        tripTypeDao = new TripTypeDao();

        Agency agency = new Agency("x12345678", "Poseidon", "POX123", 25);
        agencyDao.create(agency);
        idAgency = agencyDao.findIdByCif("x12345678");

        Client client = new Client("admin@calipso.com", "+34666777888", "Admin");
        clientDao.create(client);
        idClient = clientDao.findByMail("admin@calipso.com").getIdClient();

        Boat boat = new Boat("Queen Anne's Revenge", 75);
        boatDao.create(boat);
        idBoat = boatDao.findByName("Queen Anne's Revenge").getIdBoat();

        TripType tripType = new TripType(boat, "Sunset", Duration.ofHours(2),
                LocalTime.of(19, 0), 109.99);
        tripTypeDao.create(tripType);

        List<TripType> tripTypes = tripTypeDao.findByBoatIdAndOption(idTripType, "Sunset");
        for (TripType type : tripTypes) {
            if (type.getDepartureTime().equals(tripType.getDepartureTime())) {
                idTripType = type.getIdTripType();
            }
        }

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException{
        if (tripTypeDao.read(idTripType) != null)
            tripTypeDao.delete(idTripType);

        if (boatDao.read(idBoat) != null) {
            boatDao.delete(idBoat);
        }

        if (clientDao.read(idClient) != null) {
            clientDao.delete(idClient);
        }

        if (agencyDao.read(idAgency) != null) {
            agencyDao.delete(idAgency);
        }

    }

    void create_shouldInsertRecord() throws SQLException, ClassNotFoundException {


    }

}
