package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.model.TripType;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TripTypeDao implements CrudDao<TripType> {

    public TripTypeDao()throws SQLException, ClassNotFoundException {
    }

    @Override
    public void create(TripType tripType) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO trip_types (id_boat, trip_option, duration_minutes, departure_time, price) " +
                "values (?, ?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, tripType.getBoat().getIdBoat());
            pstmt.setString(2, tripType.getTripOption());
            pstmt.setInt(3, (int) tripType.getDuration().toMinutes());
            pstmt.setTime(4, Time.valueOf(tripType.getDepartureTime()));
            pstmt.setDouble(5, tripType.getPrice());

            pstmt.executeUpdate();

        }

    }

    @Override
    public TripType read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM trip_types WHERE id_trip_type = ?";
        TripType tripType = null;

        BoatDao boatDao = new BoatDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Boat boat = boatDao.read(rs.getInt("id_boat"));

                if (boat == null) {
                    throw new IllegalStateException("Boat does not exist.");
                }

                tripType = new TripType(
                        rs.getInt("id_trip_type"),
                        boat,
                        rs.getString("trip_option"),
                        Duration.ofMinutes(rs.getInt("duration_minutes")),
                        rs.getTime("departure_time").toLocalTime(),
                        rs.getDouble("price")
                );
            }
        }

        return tripType;
    }

    @Override
    public void update(TripType tripType) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE trip_types SET id_boat = ?, trip_option = ?, duration_minutes = ?, " +
                "departure_time = ?, price = ? WHERE id_trip_type = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, tripType.getBoat().getIdBoat());
            pstmt.setString(2, tripType.getTripOption());
            pstmt.setInt(3, (int) tripType.getDuration().toMinutes());
            pstmt.setTime(4, Time.valueOf(tripType.getDepartureTime()));
            pstmt.setDouble(5, tripType.getPrice());
            pstmt.setInt(6, tripType.getIdTripType());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM trip_types WHERE id_trip_type = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<TripType> listAll() throws SQLException, ClassNotFoundException {

        List<TripType> tripTypes = new ArrayList<>();
        BoatDao boatDao = new BoatDao();
        String sql = "SELECT * FROM trip_types";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Boat boat = boatDao.read(rs.getInt("id_boat"));

                if (boat == null) {
                    throw new IllegalStateException("Boat does not exist.");
                }

                TripType tripType = new TripType(
                        rs.getInt("id_trip_type"),
                        boat,
                        rs.getString("trip_option"),
                        Duration.ofMinutes(rs.getInt("duration_minutes")),
                        rs.getTime("departure_time").toLocalTime(),
                        rs.getDouble("price")
                );
                tripTypes.add(tripType);
            }
        }
        return tripTypes;
    }

    public List<TripType> findByBoatId(int idBoat) throws SQLException, ClassNotFoundException {

        List<TripType> tripTypes = new ArrayList<>();
        BoatDao boatDao = new BoatDao();
        String sql = "SELECT * FROM trip_types WHERE id_boat = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idBoat);

            Boat boat = boatDao.read(idBoat);

            if (boat == null) {
                throw new IllegalStateException("Boat does not exist.");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                TripType tripType = new TripType(
                        rs.getInt("id_trip_type"),
                        boat,
                        rs.getString("trip_option"),
                        Duration.ofMinutes(rs.getInt("duration_minutes")),
                        rs.getTime("departure_time").toLocalTime(),
                        rs.getDouble("price")
                );
                tripTypes.add(tripType);
            }
        }
        return tripTypes;
    }

    public List<TripType> findByBoatIdAndOption(int idBoat, String tripOption) throws SQLException, ClassNotFoundException {

        List<TripType> tripTypes = new ArrayList<>();
        BoatDao boatDao = new BoatDao();
        String sql = "SELECT * FROM trip_types WHERE id_boat = ? AND trip_option = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idBoat);
            pstmt.setString(2, tripOption);

            Boat boat = boatDao.read(idBoat);

            if (boat == null) {
                throw new IllegalStateException("Boat does not exist.");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                TripType tripType = new TripType(
                        rs.getInt("id_trip_type"),
                        boat,
                        rs.getString("trip_option"),
                        Duration.ofMinutes(rs.getInt("duration_minutes")),
                        rs.getTime("departure_time").toLocalTime(),
                        rs.getDouble("price")
                );
                tripTypes.add(tripType);
            }
        }
        return tripTypes;
    }

    public List<TripType> listAvailableTripTypesFromBoatAndDate(Boat boat, LocalDate date) throws SQLException, ClassNotFoundException {

        List<TripType> tripTypes = new ArrayList<>();

        String sql = "SELECT tt.id_trip_type,\n" +
                "       tt.trip_option,\n" +
                "       tt.duration_minutes,\n" +
                "       tt.departure_time,\n" +
                "       tt.price\n" +
                "FROM trip_types tt\n" +
                "WHERE tt.id_boat = ?\n" +
                "  AND tt.id_trip_type NOT IN (\n" +
                "    SELECT r.id_trip_type\n" +
                "    FROM reservations r\n" +
                "    WHERE r.reservation_date = ?\n" +
                "      AND r.reservation_status <> 'CANCELLED'\n" +
                ");";
        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, boat.getIdBoat());
            pstmt.setDate(2, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    tripTypes.add(new TripType(
                            rs.getInt("id_trip_type"),
                            boat,
                            rs.getString("trip_option"),
                            Duration.ofMinutes(rs.getInt("duration_minutes")),
                            rs.getTime("departure_time").toLocalTime(),
                            rs.getDouble("price")
                    ));
                }

            }

        }

        return tripTypes;

    }

}
