package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.SecurityQuestion;
import com.lluisbauza.calipso.model.TripType;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SecurityQuestionDao implements CrudDao<SecurityQuestion>{

    public SecurityQuestionDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(SecurityQuestion securityQuestion) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO security_questions (security_question) values (?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, securityQuestion.getSecurityQuestion());

            pstmt.executeUpdate();

        }

    }

    @Override
    public SecurityQuestion read(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM security_questions WHERE id_security_question = ?";
        SecurityQuestion securityQuestion = null;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                securityQuestion = new SecurityQuestion(
                        rs.getInt("id_security_question"),
                        rs.getString("security_question")
                );
            }
        }

        return securityQuestion;
    }

    @Override
    public void update(SecurityQuestion securityQuestion) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE security_questions SET security_question = ?" +
                " WHERE id_security_question = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, securityQuestion.getSecurityQuestion());
            pstmt.setInt(1, securityQuestion.getIdSecurityQuestion());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM security_questions WHERE id_security_question = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<SecurityQuestion> listAll() throws SQLException, ClassNotFoundException {

        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        String sql = "SELECT * FROM security_questions";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SecurityQuestion securityQuestion = new SecurityQuestion(
                        rs.getInt("id_security_question"),
                        rs.getString("security_question")
                );
                securityQuestions.add(securityQuestion);
            }
        }
        return securityQuestions;
    }
}
