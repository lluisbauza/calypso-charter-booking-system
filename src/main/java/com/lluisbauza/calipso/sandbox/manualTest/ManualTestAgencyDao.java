package com.lluisbauza.calipso.sandbox.manualTest;

import com.lluisbauza.calipso.dao.AgencyDao;
import com.lluisbauza.calipso.dao.BoatDao;
import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.Boat;

import java.sql.SQLException;
import java.util.List;

public class ManualTestAgencyDao {

    public static void main(String[] args) {
        try {
            testListAgencyDao();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void testListAgencyDao() throws SQLException, ClassNotFoundException {
        AgencyDao agencyDao = new AgencyDao();

        List<Agency> agencies = agencyDao.listAll();
        for (Agency agency : agencies) {
            System.out.println(agency);
            System.out.println();
        }
    }

    private static void testDeleteAgencyDao() throws SQLException, ClassNotFoundException {
        AgencyDao agencyDao = new AgencyDao();

        Agency agency = new Agency("213", "adeu", "adeuuu", 24.5);
        agencyDao.delete(agencyDao.findIdByCif(agency.getCif()));
    }

    private static void testUpdateAgencyDao() throws SQLException, ClassNotFoundException {
        AgencyDao agencyDao = new AgencyDao();

        Agency agency = new Agency("x24569834", "adeu", "adeuuu", 24.5);
        agencyDao.update(agency);
    }

    private static void testReadAgencyDao() throws SQLException, ClassNotFoundException {
        AgencyDao agencyDao = new AgencyDao();

        Agency agency = agencyDao.read(3);
        System.out.println(agency);
    }

    private static void testCreateAgencyDao() throws SQLException, ClassNotFoundException {
        AgencyDao agencyDao = new AgencyDao();

        Agency agency = new Agency("213", "adeu", "adeuuu", 24.5);
        agencyDao.create(agency);
    }
}
