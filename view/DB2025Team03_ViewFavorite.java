package view;

import model.DB2025Team03_ModelFavorite;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DB2025Team03_ViewFavorite extends JFrame {
    private JTextField userIdField;
    private JTextField facilityIdField;
    private JTextArea outputArea;

    public DB2025Team03_ViewFavorite() {
        setTitle("관심 시설 기능");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        inputPanel.add(userIdField);

        inputPanel.add(new JLabel("Facility ID:"));
        facilityIdField = new JTextField();
        inputPanel.add(facilityIdField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("좋아요 추가");
        JButton deleteBtn = new JButton("좋아요 삭제");
        JButton viewBtn = new JButton("내 좋아요 목록");
        JButton recommendBtn = new JButton("추천 시설 보기");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(recommendBtn);

        add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // 이벤트 리스너
        addBtn.addActionListener(e -> addFavorite());
        deleteBtn.addActionListener(e -> deleteFavorite());
        viewBtn.addActionListener(e -> viewFavorites());
        recommendBtn.addActionListener(e -> viewRecommended());

        setVisible(true);
    }

    // 좋아요 추가
    private void addFavorite() {
        String sql = "INSERT INTO DB2025_Favorite (user_id, facility_id) VALUES (?, ?)";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "0724");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int userId = Integer.parseInt(userIdField.getText());
            int facilityId = Integer.parseInt(facilityIdField.getText());
            ps.setInt(1, userId);
            ps.setInt(2, facilityId);
            ps.executeUpdate();
            outputArea.setText("좋아요 추가 완료");
        } catch (Exception e) {
            outputArea.setText("좋아요 추가 실패: 입력 확인 또는 중복 여부 확인");
            e.printStackTrace();
        }
    }

    // 좋아요 삭제
    private void deleteFavorite() {
        String sql = "DELETE FROM DB2025_Favorite WHERE user_id = ? AND facility_id = ?";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "0724");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int userId = Integer.parseInt(userIdField.getText());
            int facilityId = Integer.parseInt(facilityIdField.getText());
            ps.setInt(1, userId);
            ps.setInt(2, facilityId);
            ps.executeUpdate();
            outputArea.setText("좋아요 삭제 완료");
        } catch (Exception e) {
            outputArea.setText("삭제 실패: 입력 확인");
            e.printStackTrace();
        }
    }

    // 내 좋아요 목록 조회
    private void viewFavorites() {
        String sql = "SELECT facility_id FROM DB2025_Favorite WHERE user_id = ?";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "0724");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int userId = Integer.parseInt(userIdField.getText());
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder sb = new StringBuilder("내 관심 시설 목록:\n");
                boolean found = false;
                while (rs.next()) {
                    sb.append("- Facility ID: ").append(rs.getInt("facility_id")).append("\n");
                    found = true;
                }
                if (!found) sb.append("좋아요한 시설이 없습니다.");
                outputArea.setText(sb.toString());
            }

        } catch (Exception e) {
            outputArea.setText("조회 실패: 입력 확인");
            e.printStackTrace();
        }
    }

    // 추천 기능 (좋아요 많은 상위 5개 시설)
    private void viewRecommended() {
        System.out.println("추천 버튼 클릭됨");  // 1단계 확인
    
        String sql =
        "SELECT F.name, COUNT(*) AS cnt " +
        "FROM DB2025_Favorite Fav " +
        "JOIN DB2025_Facility F ON Fav.facility_id = F.facility_id " +
        "GROUP BY F.name " +
        "ORDER BY cnt DESC " +
        "LIMIT 5;";
    
        try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB2025Team03",
                    "root", "0724");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            System.out.println("쿼리 실행 성공");  // 2단계 확인
    
            StringBuilder sb = new StringBuilder("추천 시설 TOP 5:\n");
            boolean hasResult = false;
    
            while (rs.next()) {
                hasResult = true;
                sb.append("- ").append(rs.getString("name"))
                  .append(" (").append(rs.getInt("cnt")).append("명 관심)\n");
            }
    
            if (!hasResult) {
                outputArea.setText("추천할 시설이 없습니다.");
                System.out.println("추천할 시설 없음 출력됨");  // 3단계 확인
            } else {
                outputArea.setText(sb.toString());
                System.out.println("추천 결과 출력됨");  // 4단계 확인
            }
    
        } catch (SQLException e) {
            outputArea.setText("추천 시설 조회 중 오류 발생");
            e.printStackTrace();
        }
    }
    
}
