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
                "jdbc:mysql://localhost:3306/db2025team03",
                "root",
                "eeeeeeee"
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
    public void updatePetName(int pet_id, String newName) {
        String sql = "UPDATE DB2025_Pet SET name = ? WHERE pet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, pet_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePet(int pet_id) {
        String sql = "DELETE FROM DB2025_Pet WHERE pet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pet_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
