package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;
import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.*;

public class AgencyDao {
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

    public void addAgency (Agency agency) throws SQLException {
        PreparedStatement st = con.prepareStatement("INSERT INTO agencies (cif, name, affiliate_code, discount) " +
                "VALUES (?, ?, ?, ?)");

        st.setString(1, agency.getCif());
        st.setString(2, agency.getName());
        st.setString(3, agency.getAffiliateCode());
        st.setDouble(4, agency.getDiscount());

        st.executeUpdate();
        con.close();
    }

}
