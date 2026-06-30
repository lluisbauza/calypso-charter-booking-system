package com.lluisbauza.calipso.model;

public class AgencyUser {
    private int idAgencyUser;
    private Agency agency;
    private String name, mail, passwordHash;
    private boolean active;

    public AgencyUser(Agency agency, String name, String mail, String passwordHash, boolean active) {
        this.agency = agency;
        this.name = name;
        this.mail = mail;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    public AgencyUser(int idAgencyUser, Agency agency, String name, String mail, String passwordHash, boolean active) {
        this.idAgencyUser = idAgencyUser;
        this.agency = agency;
        this.name = name;
        this.mail = mail;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    public int getIdAgencyUser() {
        return idAgencyUser;
    }

    public void setIdAgencyUser(int idAgencyUser) {
        this.idAgencyUser = idAgencyUser;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}