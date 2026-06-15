package com.lluisbauza.calipso.model;

public class Boat {
    private int idBoat, capacity;
    private String boatName;

    public Boat(int idBoat, int capacity, String boatName) {
        this.idBoat = idBoat;
        this.capacity = capacity;
        this.boatName = boatName;
    }

    public int getIdBoat() {
        return idBoat;
    }

    public void setIdBoat(int idBoat) {
        this.idBoat = idBoat;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "idBoat=" + idBoat +
                ", capacity=" + capacity +
                ", boatName='" + boatName + '\'' +
                '}';
    }
}
