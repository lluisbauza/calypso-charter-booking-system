package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.PasswordHistory;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasswordHistoryDao implements CrudDao<PasswordHistory> {

    public PasswordHistoryDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(PasswordHistory passwordHistory) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO password_history (id_user, password_hash) values (?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, passwordHistory.getUser().getIdUser());
            pstmt.setString(2, passwordHistory.getPasswordHash());

            pstmt.executeUpdate();

        }

    }

    @Override
    public PasswordHistory read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM password_history WHERE id_password_history = ?";
        PasswordHistory passwordHistory = null;

        UserDao userDao = new UserDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = userDao.read(rs.getInt("id_user"));

                if (user == null) {
                    throw new IllegalStateException("User does not exist.");
                }

                passwordHistory = new PasswordHistory(
                        rs.getInt("id_password_history"),
                        user,
                        rs.getString("password_hash")
                );
            }
        }

        return passwordHistory;
    }

    @Override
    public void update(PasswordHistory passwordHistory) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE password_history SET id_user = ?, password_hash = ? WHERE id_password_history = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, passwordHistory.getUser().getIdUser());
            pstmt.setString(2, passwordHistory.getPasswordHash());
            pstmt.setInt(3, passwordHistory.getIdPasswordHistory());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM password_history WHERE id_password_history = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<PasswordHistory> listAll() throws SQLException, ClassNotFoundException {

        List<PasswordHistory> passwordHistories = new ArrayList<>();
        UserDao userDao = new UserDao();
        String sql = "SELECT * FROM password_history";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = userDao.read(rs.getInt("id_user"));

                if (user == null) {
                    throw new IllegalStateException("User does not exist.");
                }

                PasswordHistory passwordHistory = new PasswordHistory(
                        rs.getInt("id_password_history"),
                        user,
                        rs.getString("password_hash")
                );
                passwordHistories.add(passwordHistory);
            }
        }
        return passwordHistories;
    }

    public List<PasswordHistory> findByUserId(int idUser) throws SQLException, ClassNotFoundException {

        List<PasswordHistory> userPasswordList = new ArrayList<>();
        UserDao userDao = new UserDao();
        String sql = "SELECT * FROM password_history WHERE id_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idUser);

            User user = userDao.read(idUser);

            if (user == null) {
                throw new IllegalStateException("User does not exist.");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                PasswordHistory passwordHistory = new PasswordHistory(
                        rs.getInt("id_password_history"),
                        user,
                        rs.getString("password_hash")
                );
                userPasswordList.add(passwordHistory);
            }
        }
        return userPasswordList;
    }

}
