package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.AgencyPasswordHistory;
import com.lluisbauza.calipso.model.AgencyUser;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyPasswordHistoryDao implements CrudDao<AgencyPasswordHistory> {

    public AgencyPasswordHistoryDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(AgencyPasswordHistory agencyPasswordHistory) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO agency_password_history (id_agency_user, password_hash) VALUES (?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, agencyPasswordHistory.getAgencyUser().getIdAgencyUser());
            pstmt.setString(2, agencyPasswordHistory.getPasswordHash());

            pstmt.executeUpdate();
        }
    }

    @Override
    public AgencyPasswordHistory read(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM agency_password_history WHERE id_agency_password_history = ?";
        AgencyPasswordHistory agencyPasswordHistory = null;

        AgencyUserDao agencyUserDao = new AgencyUserDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                AgencyUser agencyUser = agencyUserDao.read(rs.getInt("id_agency_user"));

                if (agencyUser == null) {
                    throw new IllegalStateException("AgencyUser does not exist.");
                }

                agencyPasswordHistory = new AgencyPasswordHistory(
                        rs.getInt("id_agency_password_history"),
                        agencyUser,
                        rs.getString("password_hash")
                );
            }
        }

        return agencyPasswordHistory;
    }

    @Override
    public void update(AgencyPasswordHistory agencyPasswordHistory) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE agency_password_history SET id_agency_user = ?, password_hash = ? WHERE id_agency_password_history = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, agencyPasswordHistory.getAgencyUser().getIdAgencyUser());
            pstmt.setString(2, agencyPasswordHistory.getPasswordHash());
            pstmt.setInt(3, agencyPasswordHistory.getIdAgencyPasswordHistory());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM agency_password_history WHERE id_agency_password_history = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<AgencyPasswordHistory> listAll() throws SQLException, ClassNotFoundException {
        List<AgencyPasswordHistory> agencyPasswordHistories = new ArrayList<>();
        AgencyUserDao agencyUserDao = new AgencyUserDao();

        String sql = "SELECT * FROM agency_password_history";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AgencyUser agencyUser = agencyUserDao.read(rs.getInt("id_agency_user"));

                if (agencyUser == null) {
                    throw new IllegalStateException("AgencyUser does not exist.");
                }

                AgencyPasswordHistory agencyPasswordHistory = new AgencyPasswordHistory(
                        rs.getInt("id_agency_password_history"),
                        agencyUser,
                        rs.getString("password_hash")
                );

                agencyPasswordHistories.add(agencyPasswordHistory);
            }
        }

        return agencyPasswordHistories;
    }

    public List<AgencyPasswordHistory> findByAgencyUserId(int idAgencyUser) throws SQLException, ClassNotFoundException {
        List<AgencyPasswordHistory> agencyPasswordHistories = new ArrayList<>();
        AgencyUserDao agencyUserDao = new AgencyUserDao();

        AgencyUser agencyUser = agencyUserDao.read(idAgencyUser);

        if (agencyUser == null) {
            throw new IllegalStateException("AgencyUser does not exist.");
        }

        String sql = "SELECT * FROM agency_password_history WHERE id_agency_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idAgencyUser);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AgencyPasswordHistory agencyPasswordHistory = new AgencyPasswordHistory(
                        rs.getInt("id_agency_password_history"),
                        agencyUser,
                        rs.getString("password_hash")
                );

                agencyPasswordHistories.add(agencyPasswordHistory);
            }
        }

        return agencyPasswordHistories;
    }

    public int findIdByPassword(String password) throws SQLException, ClassNotFoundException {
        int id = -1;

        String sql = "SELECT id_agency_password_history FROM agency_password_history WHERE password_hash = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_agency_password_history");
            }
        }

        return id;
    }

    public AgencyPasswordHistory findByPassword(String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM agency_password_history WHERE password_hash = ?";
        AgencyPasswordHistory agencyPasswordHistory = null;

        AgencyUserDao agencyUserDao = new AgencyUserDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                AgencyUser agencyUser = agencyUserDao.read(rs.getInt("id_agency_user"));

                if (agencyUser == null) {
                    throw new IllegalStateException("AgencyUser does not exist.");
                }

                agencyPasswordHistory = new AgencyPasswordHistory(
                        rs.getInt("id_agency_password_history"),
                        agencyUser,
                        rs.getString("password_hash")
                );
            }
        }

        return agencyPasswordHistory;
    }
}