package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.ReservationDao;
import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.Reservation;
import com.lluisbauza.calipso.model.TripType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class ReservationService {

    private ReservationDao reservationDao = new ReservationDao();

    public ReservationService() throws SQLException, ClassNotFoundException {
    }

    /**
     * Creates a new reservation and generates a unique reservation code.
     * Reservation codes cannot be modified after creation.
     */

    public void createReservation(Reservation reservation) throws SQLException, ClassNotFoundException {

        int counter = 1;

        String reservationCode = reservation.getTripType().getBoat().getBoatName().substring(0, 2).toUpperCase() + "-"
                + reservation.getReservationDate().getYear()
                + reservation.getReservationDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).substring(0, 3).toUpperCase()
                + "-" + randomLetters(3);


        String currentReservationCode = reservationCode;

        while(affiliateCodeExists(currentReservationCode)) {
            currentReservationCode = reservationCode + counter;
            counter++;
        }

        Reservation reservationCoded = new Reservation(
                reservation.getPax(),
                currentReservationCode,
                reservation.getObservations(),
                reservation.getClient(),
                reservation.getTripType(),
                reservation.getReservationDate(),
                reservation.isAllergies(),
                reservation.getFinalPrice(),
                reservation.getAgency(),
                reservation.getReservationStatus()
        );

        reservationDao.create(reservationCoded);
    }

    private String randomLetters(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append((char) ThreadLocalRandom.current().nextInt('A', 'Z' + 1));
        }

        return sb.toString();
    }

    private boolean affiliateCodeExists(String reservationCode) throws SQLException, ClassNotFoundException {
        List<Reservation> reservations = reservationDao.listAll();
        boolean exists = false;

        for (Reservation reservation1 : reservations) {
            if (reservation1.getReservationCode().equals(reservationCode)) {
                exists = true;
            }
        }

        return exists;
    }

    // RESERVATION DATA HANDLER
    public int upcomingReservations() throws SQLException, ClassNotFoundException {

        int count = 0;
        List<Reservation> reservations = reservationDao.listAll();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().compareTo(LocalDate.now())  > 0 ) {
                count++;
            }
        }

        return count;

    }

    public int lastMonthReservations() throws SQLException, ClassNotFoundException {

        int count = 0;
        List<Reservation> reservations = reservationDao.listAll();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().getMonthValue() == LocalDate.now().getMonthValue() - 1 ) {
                count++;
            }
        }

        return count;

    }

    public int upcomingReservationsPerBoat(Boat boat) throws SQLException, ClassNotFoundException {

        int count = 0;
        List<Reservation> reservations = reservationDao.listAll();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().compareTo(LocalDate.now())  > 0 ) {
                if (reservation.getTripType().getBoat().getBoatName().equals(boat.getBoatName())) {
                    count++;
                }
            }
        }

        return count;

    }

    public int upcomingReservationsPerType(TripType tripType) throws SQLException, ClassNotFoundException {

        int count = 0;
        List<Reservation> reservations = reservationDao.listAll();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().compareTo(LocalDate.now())  > 0 ) {
                if (reservation.getTripType().getTripOption().equals(tripType.getTripOption())) {
                    count++;
                }
            }
        }

        return count;

    }

}
