package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
// import java.awt.List;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import controller.DB2025Team03_ControllerReview;
import model.DB2025Team03_ModelReviewWithUser;

import java.util.ArrayList;
import java.util.List;

/*
 * DB2025Team03_ControllerReview 클래스에 구현된 리뷰 관리 기능을 유저 인터페이스에 구현
 */

public class DB2025Team03_ViewReview extends JFrame {
    private int userId;
    private final int facilityId;
    private final DB2025Team03_ControllerReview controller;
    
    // uid, fid 입력받는 생성자
    public DB2025Team03_ViewReview(int userId, int facilityId) {
        this.userId = userId;
        this.facilityId = facilityId;
        this.controller = new DB2025Team03_ControllerReview();

        setTitle("리뷰 작성");
        setSize(400, 300);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);
    }
    
    // 6/2 수정: 전역 uid 사용하므로 fid만 입력받는 생성자 추가하여 사용
    public DB2025Team03_ViewReview(int facilityId) {
    	this.facilityId = facilityId;
        this.controller = new DB2025Team03_ControllerReview();

        JPanel reviewPanel = createReviewTablePanel(facilityId);
        setTitle("시설별 리뷰 목록");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        add(reviewPanel); 
        
        setAlwaysOnTop(true);  
        setVisible(true);
        toFront();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // 별점 입력 기능: 1~5점 중 선택하여 리뷰와 함께 등록
        JPanel ratingPanel = new JPanel(new FlowLayout());
        JLabel ratingLabel = new JLabel("별점 (1~5):");
        JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingPanel.add(ratingLabel);
        ratingPanel.add(ratingCombo);

        // 리뷰 내용 입력
        JTextArea contentArea = new JTextArea(8, 30);
        JScrollPane contentScroll = new JScrollPane(contentArea);

        // 설정된 별점과 리뷰 등록
        JButton submitBtn = new JButton("리뷰 등록");
        submitBtn.addActionListener(e -> {
            int rating = (int) ratingCombo.getSelectedItem();
            String content = contentArea.getText().trim();
            // 내용 없을 경우
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "내용을 입력하세요.");
                return;
            }

            controller.insertReviewDB2025Team03(userId, facilityId, rating, content, new Date(System.currentTimeMillis()));
            JOptionPane.showMessageDialog(this, "리뷰가 등록되었습니다!");
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitBtn);

        panel.add(ratingPanel, BorderLayout.NORTH);
        panel.add(contentScroll, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }
    
    /**
     * 시설 ID로 리뷰 조회 후 JTable을 포함하는 JPanel을 반환하는 메서드
     * 외부에서 호출해서 리뷰 목록 UI로 사용할 수 있음
     */
    public JPanel createReviewTablePanel(int facilityId) {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"작성자", "평점", "내용", "작성일"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // 편집 불가
            }
        };
        JTable table = new JTable(model);

        List<DB2025Team03_ModelReviewWithUser> reviews = controller.getReviewsByFacilityIdDB2025Team03(facilityId);

        if (reviews.isEmpty()) {
            model.addRow(new Object[]{"리뷰가 없습니다.", "", "", ""});
        } else {
            for (DB2025Team03_ModelReviewWithUser r : reviews) {
                model.addRow(new Object[]{
                    r.getName(),
                    r.getRating(),
                    r.getContent(),
                    r.getDate()
                });
            }
        }


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
