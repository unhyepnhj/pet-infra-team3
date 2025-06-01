package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DB2025Team03_ModelFacility;

public class DB2025Team03_ControllerFacility {
    private Connection conn;

    public DB2025Team03_ControllerFacility() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03",
                "DB2025Team03", //오류가 나서 root 대신 바꿨습니다.
                "DB2025Team03"
            );
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
     * CREATE TABLE DB2025_Facility (
    facility_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    opening_hours VARCHAR(50)
	);
     */
    
    // 데이터 삽입
    public void insertFacilityDB2025Team03(int facility_id, String name, String address, String category, String opening_hours) {
        String sql = "INSERT INTO DB2025_Facility (facility_id, name, address, category, opening_hours) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, facility_id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, category);
            ps.setString(5, opening_hours);
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
    public void deleteFacilityDB2025Team03(int facility_id) {
        String sql = "DELETE FROM DB2025_Facility WHERE facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, facility_id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // update
    public void updateNameDB2025Team03(int facility_id, String newName) { // 시설명 수정
        String sql = "UPDATE DB2025_Facility SET name = ? WHERE facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of updateName
    public void updateAddressDB2025Team03(int facility_id, String newAddress) { // 시설 주소 수정
        String sql = "UPDATE DB2025_Facility SET address = ? WHERE facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of updateAddress
    public void updateCategoryDB2025Team03(int facility_id, String newCategory) { // 시설 종류 수정
        String sql = "UPDATE DB2025_Facility SET category = ? WHERE facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newCategory);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of updateCategory
    public void updateOpeningHoursDB2025Team03(int facility_id, String newOpeningHours) { // 시설 오픈 시간 수정
        String sql = "UPDATE DB2025_Facility SET opening_hours = ? WHERE facility_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newOpeningHours);
            ps.setInt(2, facility_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of updateOpeningHours

    // search
    public List<DB2025Team03_ModelFacility> searchByNameDB2025Team03(String name) { // 시설명으로 검색
        String sql = "SELECT * FROM DB2025_Facility WHERE name LIKE ?";
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByName
    public List<DB2025Team03_ModelFacility> searchByAddressDB2025Team03(String address) { // 시설 주소로 검색
        String sql = "SELECT * FROM DB2025_Facility WHERE address LIKE ?";
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + address + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByAddress
    public List<DB2025Team03_ModelFacility> searchByCategoryDB2025Team03(String category) { // 시설 종류로 검색
        String sql = "SELECT * FROM DB2025_Facility WHERE category LIKE ?";
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByCategory
    public List<DB2025Team03_ModelFacility> searchByOpeningBeforeDB2025Team03(String opening_hours) { // 시설 오픈 시간으로 검색
        String sql = "SELECT * FROM DB2025_Facility WHERE LEFT(opening_hours, 5) <= ? AND SUBSTRING(opening_hours, 7, 5) >= ?";	//검색한 시간에 여는 시설
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, opening_hours);
            ps.setString(2, opening_hours);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end of searchByOpeningHours

    // 전체 조회
    public List<DB2025Team03_ModelFacility> readAllDB2025Team03() {
        String sql = "SELECT * FROM DB2025_Facility";
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
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

    // ResultSet → Model 매핑 헬퍼
    private DB2025Team03_ModelFacility mapRow(ResultSet rs) throws SQLException {
        return new DB2025Team03_ModelFacility(
            rs.getInt("facility_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getString("category"),
            rs.getString("opening_hours")
        );
    }
    
    //평균 평점 조회(검색을 위해서 추가 했어요!)
    public List<DB2025Team03_ModelFacility> searchByMinAvgRatingDB2025Team03(double minRating) {
        String sql =
          "SELECT F.* " +
          "FROM DB2025_View_TopRatedFacility V " +
          "JOIN DB2025_Facility F ON V.facility_id = F.facility_id " +
          "WHERE V.avg_rating >= ?";
        List<DB2025Team03_ModelFacility> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minRating);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}

