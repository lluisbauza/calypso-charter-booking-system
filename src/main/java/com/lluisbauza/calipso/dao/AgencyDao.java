package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyDao implements CrudDao<Agency> {

    public AgencyDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(Agency agency) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO agencies (cif, name, affiliate_code, discount) VALUES (?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, agency.getCif());
            pstmt.setString(2, agency.getName());
            pstmt.setString(3, agency.getAffiliateCode());
            pstmt.setDouble(4, agency.getDiscount());

            pstmt.executeUpdate();

        }

    }

    @Override
    public Agency read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM agencies WHERE id_agency = ?";
        Agency agency = null;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

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
    public void update(Agency agency) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE agencies SET name = ?, discount = ? WHERE cif = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, agency.getName());
            pstmt.setDouble(2, agency.getDiscount());
            pstmt.setString(3, agency.getCif());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM agencies WHERE id_agency = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<Agency> listAll() throws SQLException, ClassNotFoundException {
        List<Agency> agencies = new ArrayList<>();

        String sql = "SELECT * FROM agencies";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Agency agency = new Agency(
                        rs.getInt("id_agency"),
                        rs.getString("cif"),
                        rs.getString("name"),
                        rs.getString("affiliate_code"),
                        rs.getDouble("discount")
                );
                agencies.add(agency);
            }
        }
        return agencies;
    }

    public int findIdByCif(String cif) throws SQLException, ClassNotFoundException {
        int id = -1;

        String sql = "SELECT id_agency FROM agencies WHERE cif = ?";
        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, cif);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_agency");
            }
        }

        return id;
    }

    public Agency findByCif(String cif) throws SQLException, ClassNotFoundException {
        Agency agency = null;

        String sql = "SELECT * FROM agencies WHERE cif = ?";
        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, cif);

            ResultSet rs = pstmt.executeQuery();

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

}
