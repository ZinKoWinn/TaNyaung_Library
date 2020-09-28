package com.zinkowin.tanyaung.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private static String URL = "jdbc:mysql://localhost:3306/library_db";
	private static String USERNAME = "root";
	private static String PASSWORD = "zinkowinn";

	/*static {
		try {
			Properties prop = new Properties();
			prop.load(
					ConnectionManager.class.getResourceAsStream("resources/properties/db_sql.properties"));
			URL = prop.getProperty("DB.URL");
			USERNAME = prop.getProperty("DB.USERNAME");
			PASSWORD = prop.getProperty("DB.PASSWORD");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
