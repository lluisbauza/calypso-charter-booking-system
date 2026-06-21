package com.lluisbauza.calipso.model;

import java.time.LocalDate;

public class Reservation {
    private int idReservation, pax;
    private String reservationCode, observations;
    private Client client;
    private TripType tripType;
    private LocalDate reservationDate;
    private boolean allergies;
    private double finalPrice;
    private Agency agency;
    private ReservationStatus reservationStatus;

    public Reservation(int idReservation, int pax, String reservationCode, String observations, Client client,
                       TripType tripType, LocalDate reservationDate, boolean allergies, double finalPrice,
                       Agency agency, ReservationStatus reservationStatus) {
        this.idReservation = idReservation;
        this.pax = pax;
        this.reservationCode = reservationCode;
        this.observations = observations;
        this.client = client;
        this.tripType = tripType;
        this.reservationDate = reservationDate;
        this.allergies = allergies;
        this.finalPrice = finalPrice;
        this.agency = agency;
        this.reservationStatus = reservationStatus;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean isAllergies() {
        return allergies;
    }

    public void setAllergies(boolean allergies) {
        this.allergies = allergies;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", pax=" + pax +
                ", reservationCode='" + reservationCode + '\'' +
                ", observations='" + observations + '\'' +
                ", client=" + client +
                ", tripType=" + tripType +
                ", reservationDate=" + reservationDate +
                ", allergies=" + allergies +
                ", finalPrice=" + finalPrice +
                ", agency=" + agency +
                ", reservationStatus=" + reservationStatus +
                '}';
    }
}
