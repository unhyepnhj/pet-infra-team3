package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DB2025Team03_ModelPet;

public class DB2025Team03_ControllerPet {
    private Connection conn;

    public DB2025Team03_ControllerPet() {
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

    // CREATE
    public void insertPet(int pet_id, int user_id, String name, int age, String species) {
        String sql = "INSERT INTO DB2025_Pet VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pet_id);
            ps.setInt(2, user_id);
            ps.setString(3, name);
            ps.setInt(4, age);
            ps.setString(5, species);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 2025.05.25 수정 - 반려동물 ID 자동 배정되는 메소드 추가 
    public void insertPet2(int user_id, String name, int age, String species) {
        String getMaxIdSql = "SELECT MAX(pet_id) FROM DB2025_Pet";	// 현재 Pet 테이블에 저장된 반려동물 ID 중 가장 큰 것
        String insertSql = "INSERT INTO DB2025_Pet VALUES (?, ?, ?, ?, ?)";

        try (
            PreparedStatement ps1 = conn.prepareStatement(getMaxIdSql);
            ResultSet rs = ps1.executeQuery()
        ) {
            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }

            try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                ps2.setInt(1, nextId);
                ps2.setInt(2, user_id);
                ps2.setString(3, name);
                ps2.setInt(4, age);
                ps2.setString(5, species);
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // READ
    public List<DB2025Team03_ModelPet> readAllPets() {
        List<DB2025Team03_ModelPet> list = new ArrayList<>();
        String sql = "SELECT * FROM DB2025_Pet";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DB2025Team03_ModelPet(
                    rs.getInt("pet_id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("species")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updatePetName(int user_id, int pet_id, String newName) {	// 수정함
        String sql = "UPDATE DB2025_Pet SET name = ? WHERE user_id = ? AND pet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2,  user_id);
            ps.setInt(3, pet_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE 
    // 2025.05.25 수정 - user_id, pet_id 입력받아 삭제하는 것으로 변경
    public void deletePet(int user_id, int pet_id) {
        String sql = "DELETE FROM DB2025_Pet WHERE user_id = ? AND pet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
        	ps.setInt(2, pet_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 2025.05.25 수정 - UserId로 반려동물 검색하는 메소드 추가
    public List<DB2025Team03_ModelPet> searchByUserId(int userId) {
        List<DB2025Team03_ModelPet> list = new ArrayList<>();
        String sql = "SELECT * FROM DB2025_Pet WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DB2025Team03_ModelPet(
                        rs.getInt("pet_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("species")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
