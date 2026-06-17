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

            // para probar conexión y agencyDao.create();
//            Agency agency = new Agency("213", "adeu", "adeuuu", 24.5);
//            agencyDao.print();
//            agencyDao.create(agency);

            // para probar conexión y agencyDao.read();
            Agency agency = agencyDao.read(3);
            System.out.println(agency);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
