package com.lluisbauza;

import com.lluisbauza.calipso.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static void main() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getCon();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from users");

        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
