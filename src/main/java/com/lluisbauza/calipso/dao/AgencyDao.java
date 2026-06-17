package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyDao implements CrudDao<Agency> {
    Connection con = ConnectionManager.getCon();

    public AgencyDao() throws SQLException, ClassNotFoundException {
    }

    // para realizar pruebas
    public void print() throws SQLException, ClassNotFoundException {

        String sql = "select * from agencies";
        try(
                Connection con = ConnectionManager.getCon();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        }
    }

    @Override
    public void create(Agency agency) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO agencies (cif, name, affiliate_code, discount) VALUES (?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ){

            pstmt.setString(1, agency.getCif());
            pstmt.setString(2, agency.getName());
            pstmt.setString(3, agency.getAffiliateCode());
            pstmt.setDouble(4, agency.getDiscount());

            pstmt.executeUpdate();

        }

    }

    @Override
    public Agency read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM agencies WHERE id_agency = " + id + "";
        Agency agency = null;

        try(
                Connection con = ConnectionManager.getCon();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ){
            if (rs.next()) {
                agency = new Agency(
                        rs.getInt("id_agency"),
                        rs.getString("cif"),
                        rs.getString("name"),
                        rs.getString("affiliate_code"),
                        rs.getDouble("discount")
                );
            }
        }

        return agency;
    }

    @Override
    public void update(Agency agency) {}

    @Override
    public void delete(int id) {}

    @Override
    public List<Agency> listAll(){
        List <Agency> agencies = new ArrayList<>();
        return agencies;
    }
}
