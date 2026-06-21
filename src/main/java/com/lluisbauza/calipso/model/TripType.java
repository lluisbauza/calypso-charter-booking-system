package com.lluisbauza.calipso.model;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

public class TripType {
    private int idTripType;
    private Boat boat;
    private String tripOption;
    private Duration duration;
    private LocalTime departureTime;
    private double price;

    public TripType(int idTripType, Boat boat, String tripOption, Duration duration, LocalTime departureTime, double price) {
        this.idTripType = idTripType;
        this.boat = boat;
        this.tripOption = tripOption;
        this.duration = duration;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
                ", duration=" + duration +
                ", departureTime=" + departureTime +
                ", price=" + price +
                '}';
    }
}
