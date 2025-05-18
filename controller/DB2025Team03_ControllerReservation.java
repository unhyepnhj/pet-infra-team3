package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DB2025Team03_ModelReservation;

public class DB2025Team03_ControllerReservation {
    private Connection conn;

    public DB2025Team03_ControllerReservation() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db2025team03",
                "root",
                "eeeeeeee" 
            );
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT
    public void insertReservation(int reservationId, int userId, int facilityId, String date, String serviceType) {
        String sql = "INSERT INTO DB2025_Reservation VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ps.setInt(2, userId);
            ps.setInt(3, facilityId);
            ps.setString(4, date);
            ps.setString(5, serviceType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE: 서비스 종류만 수정
    public void updateServiceType(int reservationId, String newType) {
        String sql = "UPDATE DB2025_Reservation SET service_type = ? WHERE reservation_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newType);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //READ ALL
    public List<DB2025Team03_ModelReservation> readAllReservations() {
        List<DB2025Team03_ModelReservation> list = new ArrayList<>();
        String sql = "SELECT * FROM DB2025_Reservation";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DB2025Team03_ModelReservation r = new DB2025Team03_ModelReservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("user_id"),
                    rs.getInt("facility_id"),
                    rs.getDate("date").toString(),
                    rs.getString("service_type")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // DELETE
    public void deleteReservation(int reservationId) {
        String sql = "DELETE FROM DB2025_Reservation WHERE reservation_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SELECT
    public List<DB2025Team03_ModelReservation> getAllReservations() {
        List<DB2025Team03_ModelReservation> list = new ArrayList<>();
        String sql = "SELECT * FROM DB2025_Reservation";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DB2025Team03_ModelReservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("user_id"),
                    rs.getInt("facility_id"),
                    rs.getString("date"),
                    rs.getString("service_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
