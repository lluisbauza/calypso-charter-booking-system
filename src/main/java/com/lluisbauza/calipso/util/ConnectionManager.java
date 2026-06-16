package com.lluisbauza.calipso.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection con;

    private ConnectionManager() {}

    public static Connection getCon() throws SQLException, ClassNotFoundException {
        try {
            con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

}
