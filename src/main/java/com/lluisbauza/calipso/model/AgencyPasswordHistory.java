package com.lluisbauza.calipso.model;

public class AgencyPasswordHistory {
    private int idAgencyPasswordHistory;
    private AgencyUser agencyUser;
    private String passwordHash;

    public AgencyPasswordHistory(AgencyUser agencyUser, String passwordHash) {
        this.agencyUser = agencyUser;
        this.passwordHash = passwordHash;
    }

    public AgencyPasswordHistory(int idAgencyPasswordHistory, AgencyUser agencyUser, String passwordHash) {
        this.idAgencyPasswordHistory = idAgencyPasswordHistory;
        this.agencyUser = agencyUser;
        this.passwordHash = passwordHash;
    }

    public int getIdAgencyPasswordHistory() {
        return idAgencyPasswordHistory;
    }

    public void setIdAgencyPasswordHistory(int idAgencyPasswordHistory) {
        this.idAgencyPasswordHistory = idAgencyPasswordHistory;
    }

    public AgencyUser getAgencyUser() {
        return agencyUser;
    }

    public void setAgencyUser(AgencyUser agencyUser) {
        this.agencyUser = agencyUser;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}