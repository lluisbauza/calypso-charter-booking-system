package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.AgencyUser;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyUserDao implements CrudDao<AgencyUser> {

    public AgencyUserDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(AgencyUser agencyUser) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO agency_users (id_agency, name, mail, password_hash, active) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, agencyUser.getAgency().getIdAgency());
            pstmt.setString(2, agencyUser.getName());
            pstmt.setString(3, agencyUser.getMail());
            pstmt.setString(4, agencyUser.getPasswordHash());
            pstmt.setBoolean(5, agencyUser.isActive());

            pstmt.executeUpdate();
        }
    }

    @Override
    public AgencyUser read(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM agency_users WHERE id_agency_user = ?";
        AgencyUser agencyUser = null;

        AgencyDao agencyDao = new AgencyDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Agency agency = agencyDao.read(rs.getInt("id_agency"));

                if (agency == null) {
                    throw new IllegalStateException("Agency does not exist.");
                }

                agencyUser = new AgencyUser(
                        rs.getInt("id_agency_user"),
                        agency,
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("password_hash"),
                        rs.getBoolean("active")
                );
            }
        }

        return agencyUser;
    }

    @Override
    public void update(AgencyUser agencyUser) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE agency_users SET id_agency = ?, name = ?, mail = ?, password_hash = ?, active = ? WHERE id_agency_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, agencyUser.getAgency().getIdAgency());
            pstmt.setString(2, agencyUser.getName());
            pstmt.setString(3, agencyUser.getMail());
            pstmt.setString(4, agencyUser.getPasswordHash());
            pstmt.setBoolean(5, agencyUser.isActive());
            pstmt.setInt(6, agencyUser.getIdAgencyUser());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM agency_users WHERE id_agency_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<AgencyUser> listAll() throws SQLException, ClassNotFoundException {
        List<AgencyUser> agencyUsers = new ArrayList<>();
        AgencyDao agencyDao = new AgencyDao();

        String sql = "SELECT * FROM agency_users";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Agency agency = agencyDao.read(rs.getInt("id_agency"));

                if (agency == null) {
                    throw new IllegalStateException("Agency does not exist.");
                }

                AgencyUser agencyUser = new AgencyUser(
                        rs.getInt("id_agency_user"),
                        agency,
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("password_hash"),
                        rs.getBoolean("active")
                );

                agencyUsers.add(agencyUser);
            }
        }

        return agencyUsers;
    }

    public AgencyUser findByMail(String mail) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM agency_users WHERE mail = ?";
        AgencyUser agencyUser = null;

        AgencyDao agencyDao = new AgencyDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, mail);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Agency agency = agencyDao.read(rs.getInt("id_agency"));

                if (agency == null) {
                    throw new IllegalStateException("Agency does not exist.");
                }

                agencyUser = new AgencyUser(
                        rs.getInt("id_agency_user"),
                        agency,
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("password_hash"),
                        rs.getBoolean("active")
                );
            }
        }

        return agencyUser;
    }

    public int findIdByMail(String mail) throws SQLException, ClassNotFoundException {
        int id = -1;

        String sql = "SELECT id_agency_user FROM agency_users WHERE mail = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, mail);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_agency_user");
            }
        }

        return id;
    }

    public List<AgencyUser> findByAgencyId(int idAgency) throws SQLException, ClassNotFoundException {
        List<AgencyUser> agencyUsers = new ArrayList<>();
        AgencyDao agencyDao = new AgencyDao();

        Agency agency = agencyDao.read(idAgency);

        if (agency == null) {
            throw new IllegalStateException("Agency does not exist.");
        }

        String sql = "SELECT * FROM agency_users WHERE id_agency = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idAgency);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AgencyUser agencyUser = new AgencyUser(
                        rs.getInt("id_agency_user"),
                        agency,
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("password_hash"),
                        rs.getBoolean("active")
                );

                agencyUsers.add(agencyUser);
            }
        }

        return agencyUsers;
    }
}