package controller;

import model.DB2025Team03_ModelReservationSlot;
import java.sql.*;
import java.util.*;

/*
 * DB2025Team03_Slot 테이블에 접근하여 예약 내역 관리
 * 예약 slot을 추가하여 예약받을 수 있도록 ControllerReservation을 수정
 */

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
    
    // date 날짜에 예약 가능한 facilityId 시설의 잔여 slot을 검색
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
    
    // slotId slot이 예약되면 예약 불가 상태(is_reserved=TRUE)로 변경
    public void markSlotAsReserved(int slotId) {
    	String sql = "UPDATE DB2025_Slot SET is_reserved = TRUE WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // slotId slot의 예약이 취소되면 예약 가능 상태(is_reserved=FALSE)로 변경
    public void unmarkSlotAsReserved(int slotId) {
        String sql = "UPDATE DB2025_Slot SET is_reserved = FALSE WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // slotId slot의 slot 시간(time_range) 반환
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
