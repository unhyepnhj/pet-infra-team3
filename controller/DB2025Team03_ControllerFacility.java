package controller;

import java.sql.*;

public class DB2025Team03_ControllerFacility {
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	PreparedStatement ps = null;
	
	public DB2025Team03_ControllerFacility() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB2025Team03", "root", "DB2025Team03");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public void insertDB2025Team03(/*parameter*/) {
		
	}

}
