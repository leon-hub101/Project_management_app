package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/PoisePMS";
    private static final String USER = "otheruser";
    private static final String PASSWORD = "swordfish"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load the MySQL driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

