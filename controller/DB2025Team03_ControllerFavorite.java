package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DB2025Team03_ModelFavorite;

public class DB2025Team03_ControllerFavorite {
    private Connection conn;

    public DB2025Team03_ControllerFavorite() {
        try {
        	conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB2025Team03",
                    "root",
                    "root"
                );
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터 삽입
    public void insertFavoriteDB2025Team03(int user_id, int facility_id) {
        String sql = "INSERT INTO DB2025_Favorite (user_id, facility_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, user_id);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // 데이터 삭제
    public void deleteFavoriteDB2025Team03(int user_id, int facility_id) {
        String sql = "DELETE FROM DB2025_Favorite WHERE user_id = ? AND facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, user_id);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // update - 필요없을 듯(insert/delete만 있으면 됨)

    // search
    public List<DB2025Team03_ModelFavorite> searchByUserIdDB2025Team03(int user_id) { // 유저가 좋아요한 목록 조회
        String sql = "SELECT * FROM DB2025_Favorite WHERE user_id = ?";
        List<DB2025Team03_ModelFavorite> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByUserId
    public List<DB2025Team03_ModelFavorite> searchByFacilityIdDB2025Team03(int facility_id) { // 해당 시설을 좋아요한 유저 목록 조회
        String sql = "SELECT * FROM DB2025_Favorite WHERE facility_id = ?";
        List<DB2025Team03_ModelFavorite> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facility_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByFacilityId

    // 전체 조회
    public List<DB2025Team03_ModelFavorite> readAllDB2025Team03() {
        String sql = "SELECT * FROM DB2025_Favorite";
        List<DB2025Team03_ModelFavorite> list = new ArrayList<>();
        try (
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ResultSet → ModelFavorite 매핑 헬퍼
    private DB2025Team03_ModelFavorite mapRow(ResultSet rs) throws SQLException {
        return new DB2025Team03_ModelFavorite(
            rs.getInt("user_id"),
            rs.getInt("facility_id")
        );
    }
}
