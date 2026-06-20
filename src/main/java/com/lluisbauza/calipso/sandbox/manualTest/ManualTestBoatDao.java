package com.lluisbauza.calipso.sandbox.manualTest;

import com.lluisbauza.calipso.dao.BoatDao;
import com.lluisbauza.calipso.model.Boat;

import java.sql.SQLException;
import java.util.List;

public class ManualTestBoatDao {
    public static void main(String[] args) {
        try {
            testListBoatDao();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private static void testListBoatDao() throws SQLException, ClassNotFoundException {
        BoatDao boatDao = new BoatDao();

        List<Boat> boats = boatDao.listAll();
        for (Boat boat : boats) {
            System.out.println(boat);
            System.out.println();
        }
    }

    private static void testDeleteBoatDao() throws SQLException, ClassNotFoundException {
        BoatDao boatDao = new BoatDao();

        Boat boat = new Boat("Tomeu", 18);
        boatDao.delete(boatDao.findByName(boat.getBoatName()).getIdBoat());
    }

    private static void testUpdateBoatDao() throws SQLException, ClassNotFoundException {
        BoatDao boatDao = new BoatDao();

        Boat boat = new Boat("Tomeu", 4);
        boatDao.update(boat);
    }

    private static void testReadBoatDao() throws SQLException, ClassNotFoundException {
        BoatDao boatDao = new BoatDao();

        Boat boat = boatDao.read(3);
        System.out.println(boat);
    }

    private static void testCreateBoatDao() throws SQLException, ClassNotFoundException {
        BoatDao boatDao = new BoatDao();

        Boat boat = new Boat("Tomeu", 15);
        boatDao.create(boat);
    }
}
