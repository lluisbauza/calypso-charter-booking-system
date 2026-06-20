package com.lluisbauza.calipso.service;

import com.lluisbauza.calipso.dao.AgencyDao;
import com.lluisbauza.calipso.model.Agency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgencyService {

    private AgencyDao agencyDao = new AgencyDao();

    public AgencyService() throws SQLException, ClassNotFoundException {

    }

    public void createAgency(Agency agency) throws SQLException, ClassNotFoundException {

        int counter = 1;

        String affiliateCode = agency.getName().substring(0, 2).toUpperCase() + agency.getCif().substring(0, 4).toUpperCase();
        String currentAffiliateCode = affiliateCode;

        while(affiliateCodeExists(currentAffiliateCode)) {
            currentAffiliateCode = affiliateCode + counter;
            counter++;
        }

        Agency agencyCoded = new Agency(
                agency.getCif(),
                agency.getName(),
                currentAffiliateCode,
                agency.getDiscount()
        );

        agencyDao.create(agencyCoded);
    }

    private boolean affiliateCodeExists(String affiliateCode) throws SQLException, ClassNotFoundException {
        List<Agency> agencies = agencyDao.listAll();
        boolean exists = false;

        for (Agency agency1 : agencies) {
            if (agency1.getAffiliateCode().equals(affiliateCode)) {
                exists = true;
            }
        }

        return exists;
    }
}
