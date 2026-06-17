package com.lluisbauza;

import com.lluisbauza.calipso.dao.AgencyDao;
import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            AgencyDao agencyDao = new AgencyDao();


            Agency agency = new Agency("321", "holi", "holii", 10);
            agencyDao.print();

            agencyDao.create(agency);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
