package com.lluisbauza.calipso.model;

public class PasswordHistory {
    private int idPasswordHistory;
    private User user;
    private String passwordHash;

    public PasswordHistory(User user, String passwordHash) {
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public PasswordHistory(int idPasswordHistory, User user, String passwordHash) {
        this.idPasswordHistory = idPasswordHistory;
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public int getIdPasswordHistory() {
        return idPasswordHistory;
    }

    public void setIdPasswordHistory(int idPasswordHistory) {
        this.idPasswordHistory = idPasswordHistory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "PasswordHistory{" +
                "idPasswordHistory=" + idPasswordHistory +
                ", user=" + user +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
