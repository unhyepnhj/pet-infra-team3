package controller;

import model.DB2025Team03_ModelReservationSlot;
import java.sql.*;
import java.util.*;

public class DB2025Team03_ControllerReservationSlot {
    private Connection conn;

    public DB2025Team03_ControllerReservationSlot() {
        try {
        	conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB2025Team03",
                    "root",
                    "root"
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<DB2025Team03_ModelReservationSlot> getAvailableSlots(int facilityId, String date) {
        List<DB2025Team03_ModelReservationSlot> slots = new ArrayList<>();

        String sql = """
                SELECT S.slot_id, S.facility_id, S.time_range, S.is_reserved
                FROM DB2025_Slot S
                WHERE S.facility_id = ?
                AND slot_date = ?
                And is_reserved = 0
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facilityId);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                slots.add(new DB2025Team03_ModelReservationSlot(
                    rs.getInt("slot_id"),
                    rs.getInt("facility_id"),
                    rs.getString("time_range"),
                    rs.getBoolean("is_reserved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slots;
    }

    public void markSlotAsReserved(int slotId) {
    	String sql = "UPDATE DB2025_Slot SET is_reserved = TRUE WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void unmarkSlotAsReserved(int slotId) {
        String sql = "UPDATE DB2025_Slot SET is_reserved = FALSE WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public String getTimeRangeBySlotId(int slotId) {
        String sql = "SELECT time_range FROM DB2025_Slot WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("time_range");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
