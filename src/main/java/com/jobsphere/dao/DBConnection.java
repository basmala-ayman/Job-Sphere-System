package com.jobsphere.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//here we will use the singleton pattern to just making one database connection 

public class DBConnection {
    private static Connection connection;

    private DBConnection() {
    } // private constructor

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:postgresql://ep-round-union-ah4bjfx4-pooler.c-3.us-east-1.aws.neon.tech:5432/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_YyxqMKTA6c7e";
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
