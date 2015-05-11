package com.thinkgem.jeesite.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.thinkgem.jeesite.common.config.Global;

public class Test {

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			String lstrDriverClassName = Global.getConfig("jdbc.driver");
			Class.forName(lstrDriverClassName).newInstance();

			String lstrDataBaseUrl = Global.getConfig("jdbc.url");
			String lstrUserName = Global.getConfig("jdbc.username");
			String lstrPassword = Global.getConfig("jdbc.password");
			// String lstrSchema = Global.getConfig("jdbc.schema");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(lstrDataBaseUrl, lstrUserName, lstrPassword);

			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql = "SELECT ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_,PICTURE_ID_ FROM ACT_ID_USER";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString("ID_");
				int rev = rs.getInt("REV_");
				String first = rs.getString("FIRST_");
				String last = rs.getString("LAST_");
				String pwd = rs.getString("PWD_");
				String picture_id = rs.getString("PICTURE_ID_");
				System.out.print("ID: " + id);
				System.out.print(", Age: " + rev);
				System.out.print(", First: " + first);
				System.out.print(", Last: " + last);
				System.out.print(", password: " + pwd);
				System.out.println(", picture id: " + picture_id);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");
	}
}