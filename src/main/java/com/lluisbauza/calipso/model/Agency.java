package com.lluisbauza.calipso.model;

public class Agency {
    private int idAgency;
    private String cif, name, affiliateCode;
    private double discount;

    public Agency(int idAgency, String cif, String name, String affiliateCode, double discount) {
        this.idAgency = idAgency;
        this.cif = cif;
        this.name = name;
        this.affiliateCode = affiliateCode;
        this.discount = discount;
    }

    public int getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(int idAgency) {
        this.idAgency = idAgency;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliateCode() {
        return affiliateCode;
    }

    public void setAffiliateCode(String affiliateCode) {
        this.affiliateCode = affiliateCode;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "idAgency=" + idAgency +
                ", cif='" + cif + '\'' +
                ", name='" + name + '\'' +
                ", affiliateCode='" + affiliateCode + '\'' +
                ", discount=" + discount +
                '}';
    }
}
