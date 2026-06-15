package com.lluisbauza.calipso.model;

public class SecurityQuestion {
    private int idSecurityQuestion;
    private String securityQuestion;

    public SecurityQuestion(int idSecurityQuestion, String securityQuestion) {
        this.idSecurityQuestion = idSecurityQuestion;
        this.securityQuestion = securityQuestion;
    }

    public int getIdSecurityQuestion() {
        return idSecurityQuestion;
    }

    public void setIdSecurityQuestion(int idSecurityQuestion) {
        this.idSecurityQuestion = idSecurityQuestion;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "idSecurityQuestion=" + idSecurityQuestion +
                ", securityQuestion='" + securityQuestion + '\'' +
                '}';
    }
}
