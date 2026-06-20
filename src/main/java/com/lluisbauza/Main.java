package com.lluisbauza;

import com.lluisbauza.calipso.dao.AgencyDao;
import com.lluisbauza.calipso.dao.BoatDao;
import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.model.Boat;
import com.lluisbauza.calipso.service.AgencyService;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AgencyService agencyService = new AgencyService();

        Agency agency = new Agency("W56789345", "Venganza", 10);

        agencyService.createAgency(agency);

    }

}
