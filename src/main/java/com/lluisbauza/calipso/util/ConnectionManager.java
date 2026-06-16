package com.lluisbauza.calipso.util;

import java.sql.*;

public class ConnectionManager {
    private static Connection con;

    private ConnectionManager() {}

    public static Connection getCon() throws SQLException, ClassNotFoundException {
        con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
        return con;
    }

}
