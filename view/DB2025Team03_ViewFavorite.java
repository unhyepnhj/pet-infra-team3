package view;

import model.DB2025Team03_ModelFavorite;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/*
 * DB2025Team03_ControllerFavorite 클래스에 구현된 좋아요 기능을 유저 인터페이스에 구현
 */

public class DB2025Team03_ViewFavorite extends JFrame {
    private JTextField userIdField;
    private JTextField facilityIdField;
    private JTextArea outputArea;

    // 6/2 수정: 기본 생성자를 allGui 호출용 uid 전달받는 생성자로 변경
    public DB2025Team03_ViewFavorite(int userId) {
    	setTitle("관심 시설 기능");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 기존 uid 입력 필드에서 -> 파라미터로 전달된 uid 출력하도록 변경
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        
        inputPanel.add(new JLabel("현재 로그인 ID:"));
        JLabel userIdLabel = new JLabel(String.valueOf(userId));  // uid 텍스트로 표시
        inputPanel.add(userIdLabel);

        inputPanel.add(new JLabel("관심 시설 ID:"));
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
        
        // 좋아요 조회/등록/삭제 업데이트 내역 표시 영역
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);	// 6/2 수정
        scrollPane.setPreferredSize(new Dimension(0, 200)); 	// 높이 150
        add(scrollPane, BorderLayout.SOUTH);

        // 이벤트 리스너
        addBtn.addActionListener(e -> addFavorite(userId));
        deleteBtn.addActionListener(e -> deleteFavorite(userId));
        viewBtn.addActionListener(e -> viewFavorites(userId));
        recommendBtn.addActionListener(e -> viewRecommended());

        setVisible(true);
    }

    // 좋아요 추가
    private void addFavorite(int userId) {
        String sql = "INSERT INTO DB2025_Favorite (user_id, facility_id) VALUES (?, ?)";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "root");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // int userId = Integer.parseInt(userIdField.getText());
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
    private void deleteFavorite(int userId) {
        String sql = "DELETE FROM DB2025_Favorite WHERE user_id = ? AND facility_id = ?";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "root");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
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
    public void viewFavorites(int userId) {
        String sql = "SELECT facility_id FROM DB2025_Favorite WHERE user_id = ?";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03", "root", "root");
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
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
    
        String sql =
        "SELECT F.name, COUNT(*) AS cnt " +
        "FROM DB2025_Favorite Fav " +
        "JOIN DB2025_Facility F ON Fav.facility_id = F.facility_id " +
        "GROUP BY F.name " +
        "ORDER BY cnt DESC " +
        "LIMIT 5;";
    
        try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB2025Team03",
                    "root", "root");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            StringBuilder sb = new StringBuilder("추천 시설 TOP 5:\n");
            boolean hasResult = false;
    
            while (rs.next()) {
                hasResult = true;
                sb.append("- ").append(rs.getString("name"))
                  .append(" (").append(rs.getInt("cnt")).append("명 관심)\n");
            }
    
            if (!hasResult) {
                outputArea.setText("추천할 시설이 없습니다.");
            } else {
                outputArea.setText(sb.toString());
            }
    
        } catch (SQLException e) {
            outputArea.setText("추천 시설 조회 중 오류 발생");
            e.printStackTrace();
        }
    }
    
}
