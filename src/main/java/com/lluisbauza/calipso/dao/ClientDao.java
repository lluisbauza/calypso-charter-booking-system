package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.Client;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements CrudDao<Client> {

    public ClientDao(){}

    @Override
    public void create(Client client) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO clients (mail, phone, name) VALUES (?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ) {
            pstmt.setString(1, client.getMail());
            pstmt.setString(2, client.getPhone());
            pstmt.setString(3, client.getName());

            pstmt.executeUpdate();
        }
    }

    @Override
    public Client read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM clients WHERE id_client = ?";
        Client client = null;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("name")
                );
            }
        }

        return client;
    }

    @Override
    public void update(Client client) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE clients SET mail = ?, phone = ?, name = ? WHERE id_client = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
                ) {
            pstmt.setString(1, client.getMail());
            pstmt.setString(2, client.getMail());
            pstmt.setString(3, client.getMail());
            pstmt.setString(4, client.getMail());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM clients WHERE id_client = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
                ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<Client> listAll() throws SQLException, ClassNotFoundException {
        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("name")
                );
                clients.add(client);
            }
        }

        return clients;
    }

    public Client findByMail (String mail) throws SQLException, ClassNotFoundException {

        Client client = null;

        String sql = "SELECT * FROM clients WHERE mail = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, mail);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("name")
                );
            }
        }

        return client;
    }
}
