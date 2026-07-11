package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.dto.ReservationSummary;
import com.lluisbauza.calipso.enums.ReservationOrder;
import com.lluisbauza.calipso.enums.ReservationSearchField;
import com.lluisbauza.calipso.enums.ReservationStatus;
import com.lluisbauza.calipso.model.*;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDao implements CrudDao<Reservation> {

    public ReservationDao() throws SQLException, ClassNotFoundException{
    }

    @Override
    public void create(Reservation reservation) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO reservations (reservation_code, id_client, id_trip_type, reservation_date, " +
                "pax, allergies, final_price, id_agency, observations, reservation_status) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            if (reservation.getAgency() != null) {
                pstmt.setInt(8, reservation.getAgency().getIdAgency());
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }
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
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
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

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                Reservation reservation = new Reservation(
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
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public Reservation findByReservationCode(String code) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM reservations WHERE reservation_code = ?";
        Reservation reservation = null;

        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, code);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("pax"),
                        code,
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

    public List<Reservation> findByClientId(int idClient) throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations WHERE id_client = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idClient);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(idClient);
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                Reservation reservation = new Reservation(
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
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> findByDate(LocalDate date) throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations WHERE reservation_date = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setDate(1, Date.valueOf(date));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                Reservation reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("pax"),
                        rs.getString("reservation_code"),
                        rs.getString("observations"),
                        client, tripType, date,
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        agency, reservationStatus
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> findByTripTypeAndDate(int idTripType, LocalDate date) throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations WHERE id_trip_type = ? AND reservation_date = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idTripType);
            pstmt.setDate(2, Date.valueOf(date));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(idTripType);
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                Reservation reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("pax"),
                        rs.getString("reservation_code"),
                        rs.getString("observations"),
                        client, tripType, date,
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        agency, reservationStatus
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> findByStatus(ReservationStatus status) throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations WHERE reservation_status = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, String.valueOf(status));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int idAgency = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(idAgency);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                Reservation reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("pax"),
                        rs.getString("reservation_code"),
                        rs.getString("observations"),
                        client, tripType,
                        rs.getDate("reservation_date").toLocalDate(),
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        agency, status
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> findByAgencyId(int idAgency) throws SQLException, ClassNotFoundException {

        List<Reservation> reservations = new ArrayList<>();
        ClientDao clientDao = new ClientDao();
        TripTypeDao tripTypeDao = new TripTypeDao();
        AgencyDao agencyDao = new AgencyDao();
        String sql = "SELECT * FROM reservations WHERE id_agency = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idAgency);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = clientDao.read(rs.getInt("id_client"));
                TripType tripType = tripTypeDao.read(rs.getInt("id_trip_type"));
                int agencyId = rs.getInt("id_agency");
                Agency agency = null;

                if (!rs.wasNull()) {
                    agency = agencyDao.read(agencyId);

                    if (agency == null) {
                        throw new IllegalStateException("Agency does not exist.");
                    }
                }

                if (client == null) {
                    throw new IllegalStateException("Client does not exist.");
                }

                if (tripType == null) {
                    throw new IllegalStateException("TripType does not exist.");
                }

                String status = rs.getString("reservation_status");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

                Reservation reservation = new Reservation(
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
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public int countReservedSeats(int idTripType, LocalDate date) throws SQLException, ClassNotFoundException {

        int reservedSeats = 0;

        String sql = "SELECT sum(pax) AS reserved_seats FROM reservations WHERE id_trip_type = ? AND reservation_date = ?" +
                "AND reservation_status NOT IN ('CANCELLED', 'REFUNDED')";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, idTripType);
            pstmt.setDate(2, Date.valueOf(date));

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                reservedSeats = rs.getInt("reserved_seats");
            }
        }

        return reservedSeats;
    }

    public Map<String, Integer> countReservationsPerType() throws SQLException, ClassNotFoundException {

        Map<String, Integer> reservations = new HashMap<>();

        String sql = "SELECT boats.boat_name AS Name, COUNT(reservations.id_reservation) AS Total FROM boats\n" +
                "    JOIN trip_types ON trip_types.id_boat = boats.id_boat\n" +
                "    LEFT JOIN reservations ON reservations.id_trip_type = trip_types.id_trip_type\n" +
                "GROUP BY Name;";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String boatName = rs.getString("Name");
                int totalReservationsPerBoat = rs.getInt("Total");
                reservations.put(boatName, totalReservationsPerBoat);
            }
        }
        return reservations;
    }

    public List<ReservationSummary> listReservationSummariesOrderedBy(ReservationOrder order)
            throws SQLException, ClassNotFoundException {

        List<ReservationSummary> summaries = new ArrayList<>();

        String orderBy = switch (order) {
            case CODE -> "r.reservation_code";
            case CLIENT -> "c.name";
            case TRIP_OPTION -> "tt.trip_option";
            case BOAT -> "b.boat_name";
            case DATE -> "r.reservation_date";
            case PAX -> "r.pax";
            case PRICE -> "r.final_price";
            case STATUS -> "r.reservation_status";
        };

        String sql = "SELECT r.id_reservation,\n" +
                "       r.reservation_code,\n" +
                "       c.name AS client_name,\n" +
                "       b.boat_name,\n" +
                "       tt.trip_option,\n" +
                "       r.reservation_date,\n" +
                "       r.pax,\n" +
                "       r.allergies,\n" +
                "       r.final_price,\n" +
                "       r.observations,\n" +
                "       r.reservation_status\n" +
                "FROM reservations r\n" +
                "JOIN clients c ON r.id_client = c.id_client\n" +
                "JOIN trip_types tt ON r.id_trip_type = tt.id_trip_type\n" +
                "JOIN boats b ON tt.id_boat = b.id_boat\n" +
                "ORDER BY " + orderBy;

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {

                ReservationStatus reservationStatus =
                        ReservationStatus.valueOf(rs.getString("reservation_status"));

                ReservationSummary summary = new ReservationSummary(
                        rs.getInt("id_reservation"),
                        rs.getString("reservation_code"),
                        rs.getString("client_name"),
                        rs.getString("boat_name"),
                        rs.getString("trip_option"),
                        rs.getDate("reservation_date").toLocalDate(),
                        rs.getInt("pax"),
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        rs.getString("observations"),
                        reservationStatus
                );

                summaries.add(summary);
            }
        }

        return summaries;
    }
    
    public List<ReservationSummary> findReservationSummaryByField(ReservationSearchField field, String value)
            throws SQLException, ClassNotFoundException {

        List<ReservationSummary> summaries = new ArrayList<>();

        String searchField = switch (field) {
            case CODE -> "r.reservation_code";
            case NAME -> "c.name";
            case MAIL -> "c.mail";
        };

        String sql = "SELECT r.id_reservation,\n" +
                "       r.reservation_code,\n" +
                "       c.name AS client_name,\n" +
                "       b.boat_name,\n" +
                "       tt.trip_option,\n" +
                "       r.reservation_date,\n" +
                "       r.pax,\n" +
                "       r.allergies,\n" +
                "       r.final_price,\n" +
                "       r.observations,\n" +
                "       r.reservation_status\n" +
                "FROM reservations r\n" +
                "JOIN clients c ON r.id_client = c.id_client\n" +
                "JOIN trip_types tt ON r.id_trip_type = tt.id_trip_type\n" +
                "JOIN boats b ON tt.id_boat = b.id_boat\n" +
                "WHERE " + searchField + " = ?";

        try (
                Connection con = ConnectionManager.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                ReservationStatus reservationStatus =
                        ReservationStatus.valueOf(rs.getString("reservation_status"));

                ReservationSummary summary = new ReservationSummary(
                        rs.getInt("id_reservation"),
                        rs.getString("reservation_code"),
                        rs.getString("client_name"),
                        rs.getString("boat_name"),
                        rs.getString("trip_option"),
                        rs.getDate("reservation_date").toLocalDate(),
                        rs.getInt("pax"),
                        rs.getBoolean("allergies"),
                        rs.getDouble("final_price"),
                        rs.getString("observations"),
                        reservationStatus
                );

                summaries.add(summary);

            }
        }

        return summaries;
    }



}
