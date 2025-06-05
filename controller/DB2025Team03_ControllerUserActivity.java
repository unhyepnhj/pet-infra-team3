package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DB2025Team03_ModelUserActivity;

/*
 * DB2025_View_UserActivity 테이블에 접근하여 요약된 유저 활동 내역 관리
 * 활동 내역에는 지금까지 유저가 등록한 총 예약 건수, 총 리뷰 건수가 저장됨
 * view로 구현
 */

public class DB2025Team03_ControllerUserActivity {
    private Connection conn;

    // 생성자: DB 연결
    public DB2025Team03_ControllerUserActivity() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03",
                "root",
                "root"
            );
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 자원 해제
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 특정 사용자의 예약 + 리뷰 요약 조회
    public DB2025Team03_ModelUserActivity getUserActivitySummary(int userId) {
        String sql = "SELECT * FROM DB2025_View_UserActivity WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DB2025Team03_ModelUserActivity(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("total_reservations"),
                        rs.getInt("total_reviews")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}