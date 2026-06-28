package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.*;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.time.Duration;
import java.util.List;

public class ReservationDao implements CrudDao<Reservation> {

    public ReservationDao() throws SQLException, ClassNotFoundException{
    }

    @Override
    public void create(Reservation reservation) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO trip_types (reservation_code, id_client, id_trip_type, reservation_date, " +
                "pax, allergies, final_price, id_agency, observations, reservation_status) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, reservation.getReservationCode());
            pstmt.setInt(2, reservation.getClient().getIdClient());
            pstmt.setInt(3, reservation.getTripType().getIdTripType());
            pstmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            pstmt.setInt(5, reservation.getPax());
            pstmt.setBoolean(6, reservation.isAllergies());
            pstmt.setDouble(7, reservation.getFinalPrice());
            pstmt.setInt(8, reservation.getAgency().getIdAgency());
            pstmt.setString(9, reservation.getObservations());
            pstmt.setString(10, reservation.getReservationStatus().name());

            pstmt.executeUpdate();

        }

    }

    @Override
    public Reservation read(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM reservations WHERE id_reservation = ?";
        Reservation reservation = null;

        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                Agency agency = agencyDao.read(rs.getInt("id_agency"));

                if (client == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                if (agency == null) {
                    throw new IllegalStateException("Agency does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("pax"),
                        rs.getString("reservation_code"),
                        rs.getString("observations"),
                        client, tripType,
                        rs.getDate("reservation_date").toLocalDate(),
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        agency, reservationStatus
                );
            }
        }

        return reservation;
    }

    @Override
    public void update(Reservation reservation) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE reservations SET id_trip_type = ?, reservation_date = ?, pax = ?, allergies = ?, " +
                "final_price = ?, observations = ?, reservation_status = ? WHERE id_reservation = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, reservation.getTripType().getIdTripType());
            pstmt.setDate(2, Date.valueOf(reservation.getReservationDate()));
            pstmt.setInt(3, reservation.getPax());
            pstmt.setBoolean(4, reservation.isAllergies());
            pstmt.setDouble(5, reservation.getFinalPrice());
            pstmt.setString(6, reservation.getObservations());
            pstmt.setString(7, reservation.getReservationStatus().name());
            pstmt.setInt(8, reservation.getIdReservation());

            pstmt.executeUpdate();

        }

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM reservations WHERE id_reservation = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

    }

    @Override
    public List<Reservation> listAll() throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
