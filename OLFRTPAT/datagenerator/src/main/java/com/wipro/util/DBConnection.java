/* data insertion and data reading from MySQL*/

package com.wipro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

	public static Connection connectionClose(Connection conn) throws Exception {
		try {
			conn.close();
			System.out.println("Datadase connection closed..!");
		} // end of try block
		catch (SQLException e) {
			System.out.println(e);
		}
		return conn;
	}

	public static Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "Jdbc:mysql://localhost/onlinebot";
			String username = "root";
			String password = "root";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection established with database..!");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	public static Connection insertIntoDatabase(Connection conn, String weights, String logicset) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement("update logicset set weights=? where algorithm=?");
			pst.setString(1, weights);
			pst.setString(2, logicset);
			pst.executeUpdate();
			System.out.println("Data updated Successful..!");
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	/**
	 * This main class establishing the database connection, inserting data to
	 * database,reading data from database, and closing the connection after all
	 * operations
	 */
	public static void main(String[] args) throws Exception {

	}

	public static Map<String, Object> readFromDatabase(Connection conn, String query) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		while (rs.next()) {
			while (columns > 0) {
				map.put(rsmd.getColumnName(columns), rs.getString(columns));
				columns--;
			}
		}
		System.out.println("Data Reading Successful..!");
		return map;
	}

	public static String readWeightsFromDB(Connection conn, String query) throws Exception {
		String str = null;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			str = rs.getString(1);
		}
		return str;
	}
}
