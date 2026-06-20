package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoatDao implements CrudDao<Boat> {

    public BoatDao() {
    }

    @Override
    public void create(Boat boat) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO boats (boat_name, capacity) VALUES (?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, boat.getBoatName());
            pstmt.setInt(2, boat.getCapacity());

            pstmt.executeUpdate();

        }

    }

    @Override
    public Boat read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM boats WHERE id_boat = ?";
        Boat boat = null;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                boat = new Boat(
                        rs.getInt("id_boat"),
                        rs.getString("boat_name"),
                        rs.getInt("capacity")
                );
            }
        }

        return boat;
    }

    @Override
    public void update(Boat boat) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE boats SET boat_name = ?, capacity = ? WHERE id_boat = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, boat.getBoatName());
            pstmt.setInt(2, boat.getCapacity());
            pstmt.setInt(3, boat.getIdBoat());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM boats WHERE id_boat = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Boat> listAll() throws SQLException, ClassNotFoundException {
        List<Boat> boats = new ArrayList<>();

        String sql = "SELECT * FROM boats";
        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Boat boat = new Boat(
                        rs.getInt("id_boat"),
                        rs.getString("boat_name"),
                        rs.getInt("capacity")
                );
                boats.add(boat);
            }
        }
        return boats;
    }

    public Boat findByName (String name) throws SQLException, ClassNotFoundException {

        Boat boat = null;

        String sql = "SELECT * FROM boats WHERE boat_name = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
                ) {
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                boat = new Boat(
                        rs.getInt("id_boat"),
                        rs.getString("boat_name"),
                        rs.getInt("capacity")
                );
            }
        }

        return boat;
    }
}
