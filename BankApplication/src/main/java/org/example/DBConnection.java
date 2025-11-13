package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/cli_bank_db";
    private static final String USER = "root";
    private static final String PASS = "pradeep";

    public static Connection getConnection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQl JDBC Driver not found");
            throw new SQLException("Driver not found",e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
