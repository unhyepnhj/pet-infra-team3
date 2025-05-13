package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB2025Team03_ControllerUser {
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	PreparedStatement ps = null;
	
	public DB2025Team03_ControllerUser() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB2025Team03", "root", "DB2025Team03");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
