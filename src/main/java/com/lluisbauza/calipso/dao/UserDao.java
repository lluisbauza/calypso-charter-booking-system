package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements CrudDao<User> {

    public UserDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(User user) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO users (first_name, last_name_1, last_name_2, current_password_hash, " +
                "id_security_question, security_answer, mail, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName1());
            pstmt.setString(3, user.getLastName2());
            pstmt.setString(4, user.getCurrentPasswordHash());
            pstmt.setInt(5, user.getIdSecurityQuestion());
            pstmt.setString(6, user.getSecurityAnswer());
            pstmt.setString(7, user.getMail());
            pstmt.setString(8, user.getUsername());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setIdUser(rs.getInt(1));
                }
            }

        }

    }

    @Override
    public User read(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        User user = null;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id_user"),
                        rs.getInt("id_security_question"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name_1"),
                        rs.getString("last_name_2"),
                        rs.getString("mail"),
                        rs.getBoolean("must_change_password"),
                        rs.getString("current_password_hash"),
                        rs.getString("security_answer")
                );
            }
        }

        return user;
    }

    @Override
    public void update(User user) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE users SET first_name = ?, last_name_1 = ?, last_name_2 = ?, must_change_password = ?, " +
                "current_password_hash = ?, id_security_question = ?, security_answer = ?, mail = ?, username = ? WHERE id_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName1());
            pstmt.setString(3, user.getLastName2());
            pstmt.setBoolean(4, user.isMustChangePassword());
            pstmt.setString(5, user.getCurrentPasswordHash());
            pstmt.setInt(6, user.getIdSecurityQuestion());
            pstmt.setString(7, user.getSecurityAnswer());
            pstmt.setString(8, user.getMail());
            pstmt.setString(9, user.getUsername());
            pstmt.setInt(10, user.getIdUser());

            pstmt.executeUpdate();

        }
    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM users WHERE id_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<User> listAll() throws SQLException, ClassNotFoundException {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id_user"),
                        rs.getInt("id_security_question"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name_1"),
                        rs.getString("last_name_2"),
                        rs.getString("mail"),
                        rs.getBoolean("must_change_password"),
                        rs.getString("current_password_hash"),
                        rs.getString("security_answer")
                );
                users.add(user);
            }
        }

        return users;

    }

    public User findByMail(String mail) throws SQLException, ClassNotFoundException {

        User user = null;

        String sql = "SELECT * FROM users WHERE mail = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, mail);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id_user"),
                        rs.getInt("id_security_question"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name_1"),
                        rs.getString("last_name_2"),
                        rs.getString("mail"),
                        rs.getBoolean("must_change_password"),
                        rs.getString("current_password_hash"),
                        rs.getString("security_answer")
                );

            }
        }

        return user;
    }

    public void updatePassword(User user) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE users SET must_change_password = ?, current_password_hash = ?  WHERE id_user = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {

            pstmt.setBoolean(1, user.isMustChangePassword());
            pstmt.setString(2, user.getCurrentPasswordHash());
            pstmt.setInt(3, user.getIdUser());

            pstmt.executeUpdate();

        }
    }


}
