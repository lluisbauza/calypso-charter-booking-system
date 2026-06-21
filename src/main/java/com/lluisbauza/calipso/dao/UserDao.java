package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.User;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements CrudDao<User> {

    public UserDao() throws SQLException, ClassNotFoundException {}

    @Override
    public void create(User user) throws SQLException, ClassNotFoundException {

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

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

    }

    @Override
    public List<User> listAll() throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
