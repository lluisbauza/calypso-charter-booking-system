package com.lluisbauza.calipso.model;

public class Client {
    private int idClient;
    private String mail, phone, name;

    public Client(int idClient, String mail, String phone, String name) {
        this.idClient = idClient;
        this.mail = mail;
        this.phone = phone;
        this.name = name;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
