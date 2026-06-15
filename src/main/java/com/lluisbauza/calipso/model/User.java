package com.lluisbauza.calipso.model;

public class User {
    private int idUser, idSecurityQuestion;
    private String username, firstName, lastName1, lastName2, mail;
    private boolean mustChangePassword;
    private String currentPasswordHash, securityAnswer;

    public User(int idUser, int idSecurityQuestion, String username, String firstName, String lastName1, String lastName2,
                String mail, boolean mustChangePassword, String currentPasswordHash, String securityAnswer) {
        this.idUser = idUser;
        this.idSecurityQuestion = idSecurityQuestion;
        this.username = username;
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.mail = mail;
        this.mustChangePassword = mustChangePassword;
        this.currentPasswordHash = currentPasswordHash;
        this.securityAnswer = securityAnswer;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdSecurityQuestion() {
        return idSecurityQuestion;
    }

    public void setIdSecurityQuestion(int idSecurityQuestion) {
        this.idSecurityQuestion = idSecurityQuestion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public String getCurrentPasswordHash() {
        return currentPasswordHash;
    }

    public void setCurrentPasswordHash(String currentPasswordHash) {
        this.currentPasswordHash = currentPasswordHash;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", idSecurityQuestion=" + idSecurityQuestion +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName1='" + lastName1 + '\'' +
                ", lastName2='" + lastName2 + '\'' +
                ", mail='" + mail + '\'' +
                ", mustChangePassword=" + mustChangePassword +
                ", currentPasswordHash='" + currentPasswordHash + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                '}';
    }
}
