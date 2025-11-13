package com.pradeep;

import java.sql.*;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/stumangdb";
	private static final String USER = "root";
	private static final String PASS = "pradeep";
	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		try {
			if(connection==null || connection.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(URL,USER,PASS);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
}
