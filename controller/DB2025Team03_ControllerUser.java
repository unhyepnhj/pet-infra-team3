package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DB2025Team03_ModelUser;

/*
 * DB2025Team03_User 테이블에 접근하여 유저 데이터 관리
 */

public class DB2025Team03_ControllerUser {
    private Connection conn;

    // 생성자: DB 연결 + 드라이버 명시
    public DB2025Team03_ControllerUser() {
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

    // 자원 해제 메서드
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 중복 체크
    public boolean isUserExistDB2025Team03(int user_id) {
        String sql = "SELECT user_id FROM DB2025_User WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 사용자 등록
    public void insertUserDB2025Team03(int user_id, String name, int birth_year, String gender, String email) {
        String sql = "INSERT INTO DB2025_User (user_id, name, birth_year, gender, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, user_id);
            ps.setString(2, name);
            ps.setInt(3, birth_year);	// setString -> setInt 수정
            ps.setString(4, gender);
            ps.setString(5, email);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // 사용자 삭제
    public void deleteUserDB2025Team03(int user_id) {
        String sql = "DELETE FROM DB2025_User WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, user_id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // UPDATE
    // 사용자 아이디=user_id인 사용자의 이메일을 newEmail로 변경
    public void updateEmailDB2025Team03(int user_id, String newEmail) {
        String sql = "UPDATE DB2025_User SET email = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, user_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 사용자 아이디=user_id인 사용자의 이름을 newName으로 변경
    public void updateNameDB2025Team03(int user_id, String newName) {
        String sql = "UPDATE DB2025_User SET name = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, user_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 사용자 아이디=user_id인 사용자의 생일을 newBirtyYear로 변경
    public void updateBirthYearDB2025Team03(int user_id, int newBirthYear) {
        String sql = "UPDATE DB2025_User SET birth_year = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newBirthYear);
            ps.setInt(2, user_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 사용자 아이디=user_id인 사용자의 성별을 newGender로 변경
    public void updateGenderDB2025Team03(int user_id, String newGender) {
        String sql = "UPDATE DB2025_User SET gender = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newGender);
            ps.setInt(2, user_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 전체 사용자 조회
    public List<DB2025Team03_ModelUser> readAllUsers() {
        String sql = "SELECT * FROM DB2025_User";
        List<DB2025Team03_ModelUser> list = new ArrayList<>();
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

    // 사용자 아이디=user_id인 사용자 검색
    public DB2025Team03_ModelUser searchByIdDB2025Team03(int user_id) {
        String sql = "SELECT * FROM DB2025_User WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 사용자 이메일=email인 사용자 검색
        public List<DB2025Team03_ModelUser> searchByEmailDB2025Team03(String email) {
        String sql = "SELECT * FROM DB2025_User WHERE email LIKE ?";
        List<DB2025Team03_ModelUser> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + email + "%");
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
        
    // 동명이인 구분
    public DB2025Team03_ModelUser searchByNameAndBirthYearDB2025Team03(String name, int birthYear) {
        String sql = "SELECT * FROM DB2025_User WHERE name = ? AND birth_year = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, birthYear);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
    

    // ResultSet → Model 매핑
    private DB2025Team03_ModelUser mapRow(ResultSet rs) throws SQLException {
        return new DB2025Team03_ModelUser(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getInt("birth_year"),
            rs.getString("gender"),
            rs.getString("email")
        );
    }
}
