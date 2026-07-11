package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.BoatDao;
import com.lluisbauza.calipso.dao.ReservationDao;
import com.lluisbauza.calipso.dto.ReservationSummary;
import com.lluisbauza.calipso.enums.ReservationOrder;
import com.lluisbauza.calipso.enums.ReservationSearchField;
import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
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

    // RESERVATION BASIC STATISTICS

    public int upcomingReservations() throws SQLException, ClassNotFoundException {

        int count = 0;
        List<Reservation> reservations = reservationDao.listAll();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().isAfter(LocalDate.now())) {
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

    public Map<String, Integer> upcomingReservationsPerBoat() throws SQLException, ClassNotFoundException {

        Map<String, Integer> reservationsPerBoat = new HashMap<>();
        BoatDao boatDao = new BoatDao();
        List<Boat> boats = boatDao.listAll();

        List<Reservation> reservations = reservationDao.listAll();

        for (Boat boat : boats) {

            String boatName = boat.getBoatName();
            reservationsPerBoat.put(boatName, 0);

            for (Reservation reservation : reservations) {
                if (reservation.getReservationDate().isAfter(LocalDate.now())) {
                    if (reservation.getTripType().getBoat().getBoatName().equals(boatName)) {
                        reservationsPerBoat.put(boatName, reservationsPerBoat.get(boatName) + 1);
                    }
                }
            }
        }

        return reservationsPerBoat;

    }

    public Map<String, Integer> upcomingReservationsPerType() throws SQLException, ClassNotFoundException {

        return reservationDao.countReservationsPerType();

    }

    // RESERVATION FULL DISPLAY AND ORDER/FILTER
    public List<ReservationSummary> listReservationSummariesOrderedBy(ReservationOrder order) throws SQLException, ClassNotFoundException {

        return reservationDao.listReservationSummariesOrderedBy(order);

    }

    public List<ReservationSummary> findReservationSummaryByField(ReservationSearchField field, String value) throws Exception {

        return reservationDao.findReservationSummaryByField(field, value);

    }


}
