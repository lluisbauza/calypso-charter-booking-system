package com.lluisbauza.calipso.model;

import java.time.LocalTime;

public class TripType {
    private int idTripType;
    private Boat boat;
    private String tripOption;
    private LocalTime departureTime;
    private double price;

    public TripType(int idTripType, Boat boat, String tripOption, LocalTime departureTime, double price) {
        this.idTripType = idTripType;
        this.boat = boat;
        this.tripOption = tripOption;
        this.departureTime = departureTime;
        this.price = price;
    }

    public int getIdTripType() {
        return idTripType;
    }

    public void setIdTripType(int idTripType) {
        this.idTripType = idTripType;
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public String getTripOption() {
        return tripOption;
    }

    public void setTripOption(String tripOption) {
        this.tripOption = tripOption;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TripType{" +
                "idTripType=" + idTripType +
                ", boat=" + boat +
                ", tripOption='" + tripOption + '\'' +
                ", departureTime=" + departureTime +
                ", price=" + price +
                '}';
    }
}
