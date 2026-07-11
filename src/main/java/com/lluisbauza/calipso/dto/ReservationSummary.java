package com.lluisbauza.calipso.dto;

import com.lluisbauza.calipso.enums.ReservationStatus;

import java.time.LocalDate;

public class ReservationSummary {
    private int idReservation;
    private String reservationCode;
    private String clientName;
    private String boatName;
    private String tripOption;
    private LocalDate reservationDate;
    private int pax;
    private boolean allergies;
    private double finalPrice;
    private String observations;
    private ReservationStatus status;

    public ReservationSummary(int idReservation, String reservationCode, String clientName, String boatName,
                              String tripOption, LocalDate reservationDate, int pax, boolean allergies,
                              double finalPrice, String observations, ReservationStatus status) {
        this.idReservation = idReservation;
        this.reservationCode = reservationCode;
        this.clientName = clientName;
        this.boatName = boatName;
        this.tripOption = tripOption;
        this.reservationDate = reservationDate;
        this.pax = pax;
        this.allergies = allergies;
        this.finalPrice = finalPrice;
        this.observations = observations;
        this.status = status;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getTripOption() {
        return tripOption;
    }

    public void setTripOption(String tripOption) {
        this.tripOption = tripOption;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReservationSummary{" +
                "idReservation=" + idReservation +
                ", reservationCode='" + reservationCode + '\'' +
                ", clientName='" + clientName + '\'' +
                ", boatName='" + boatName + '\'' +
                ", tripOption='" + tripOption + '\'' +
                ", reservationDate=" + reservationDate +
                ", pax=" + pax +
                ", allergies=" + allergies +
                ", finalPrice=" + finalPrice +
                ", observations='" + observations + '\'' +
                ", status=" + status +
                '}';
    }
}
