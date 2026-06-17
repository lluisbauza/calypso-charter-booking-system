package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyDao implements CrudDao<Agency> {
    Connection con = ConnectionManager.getCon();

    public AgencyDao() throws SQLException, ClassNotFoundException {
    }

    public void print() throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from agencies");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    @Override
    public void create(Agency agency) throws SQLException {
        PreparedStatement st = con.prepareStatement("INSERT INTO agencies (cif, name, affiliate_code, discount) " +
                "VALUES (?, ?, ?, ?)");

        st.setString(1, agency.getCif());
        st.setString(2, agency.getName());
        st.setString(3, agency.getAffiliateCode());
        st.setDouble(4, agency.getDiscount());

        st.executeUpdate();
        con.close();
    }

    @Override
    public Agency read(int id) {
        return null;
    }

    @Override
    public void update(Agency agency) {}

    @Override
    public void delete(int id) {}

    @Override
    public List<Agency> listAll(){
        List <Agency> agencies = new ArrayList<>();
        return agencies;
    }
}
