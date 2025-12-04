package com.jobsphere.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//here we will use the singleton pattern to just making one database connection 

public class DBConnection {
    private static Connection connection;

    private DBConnection() {} // private constructor

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
          String url = "jdbc:postgresql://localhost:5432/jobsphere";
          String user = "postgres";
            String password = "habiba";
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
