package allGui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import view.DB2025Team03_ViewFavorite;
import view.DB2025Team03_ViewManage;
import view.DB2025Team03_ViewReview;
import view.DB2025Team03_ViewSearch;

/*
 * 유저 인터페이스 구현
 */

public class DB2025Team03_allGui {

    private JFrame   frame;
    private JPanel   panel;
    private JButton btnLogin;
    private JButton  btnFacility;
    private JButton btnManage;
    private JButton  btnReview;
    private JButton  btnMyFavorite;

    public DB2025Team03_allGui() {
        initGui();
    }

    // GUI 실행 시작: 메인 화면 생성 및 컴포넌트 부착
    private void initGui() {
        frame = new JFrame("DB2025Team03 메인 메뉴");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setBackground(new Color(200, 200, 200));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(panel);

        // 타이틀
        JLabel title = new JLabel("DB2025Team03  메인 메뉴", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        
        btnLogin = makeButton("로그인");
        panel.add(btnLogin);
        btnLogin.addActionListener(e -> showLoginDialog());

        // 버튼 생성
        btnFacility     = makeButton("시설 검색");
        btnManage       = makeButton("내 정보 관리");
        btnMyFavorite   = makeButton("내 시설 관리");	// 좋아요 추가 등등..
        btnReview       = makeButton("리뷰 검색");

        panel.add(btnFacility);
        panel.add(btnManage);
        panel.add(btnReview);
        panel.add(btnMyFavorite);

// 버튼 클릭 이벤트 연결
        // 시설 검색(및 조회)
        btnFacility.addActionListener(e -> {
            try {
                // 시설 검색 Swing 뷰 띄우기
                new DB2025Team03_ViewSearch().showMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                    "DB 연결 오류:\n" + ex.getMessage(),
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
            // 메뉴 창 숨기기
            frame.setVisible(true);	// false->true 수정해서 취소 누르면 메인 화면으로 돌아오도록 하였습니다
        });
        
        // 내 정보 관리(반려동물 관리, 예약 내역 조회)
        btnManage.addActionListener(e -> {
        	if (DB2025Team03_Session.userId == 0) {
                JOptionPane.showMessageDialog(frame, "로그인 후 이용 가능합니다.");
            } else {
                new DB2025Team03_ViewManage(DB2025Team03_Session.userId); // 전달
            }
			
            frame.setVisible(true);
        });
        
        // 리뷰 검색
        btnReview.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "리뷰를 조회할 시설 ID를 입력하세요:");
            if (input == null) { // 취소 눌렀을 경우
                return;
            }
            int facilityId = 0;
            try {
                facilityId = Integer.parseInt(input.trim());
                if (facilityId <= 0) {
                    JOptionPane.showMessageDialog(frame, "유효한 시설 ID를 입력해주세요.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "시설 ID는 숫자로 입력해주세요.");
                return;
            }

            new DB2025Team03_ViewReview(facilityId).createReviewTablePanel(facilityId);
            frame.setVisible(true);
        });

        frame.setVisible(true);
        
     // 좋아요한 시설 조회
        btnMyFavorite.addActionListener(e -> {
        	if (DB2025Team03_Session.userId == 0) {
                JOptionPane.showMessageDialog(frame, "로그인 후 이용 가능합니다.");
            } else {
                new DB2025Team03_ViewFavorite(DB2025Team03_Session.userId); // 전달
            }
			
            frame.setVisible(true);
        });
        
    }
     
    // 6.2 수정: 로그인 이벤트 추가
    /*
     * 로그인 팝업 -> ID/PW 입력(DB 구조상 비밀번호 사용하지 않으므로 임의 값 입력하고 아래에서는 무시)
     * 입력한 ID를 전역 세션으로 저장하여 외부 클래스와 연동 -> 패키지 내 Session 클래스 추가
     */
    
    private void showLoginDialog() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        JTextField userIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("사용자 ID:"));
        loginPanel.add(userIdField);
        loginPanel.add(Box.createVerticalStrut(10));

        loginPanel.add(new JLabel("비밀번호:"));
        loginPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(frame, loginPanel, "로그인",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int userId = Integer.parseInt(userIdField.getText());
            // char[] password = passwordField.getPassword(); 		// 사용 안 함

            if (userId != 0) {
            	DB2025Team03_Session.userId = userId;	// 세션에 추가 -> 프로그램 전체에서 사용
                JOptionPane.showMessageDialog(frame,
                        "로그인 되었습니다: " + userId,
                        "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "사용자 ID를 입력하세요.",
                        "입력 오류", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    // 공통 버튼 생성 헬퍼
    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(500, 50));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFont(new Font("Dialog", Font.PLAIN, 18));
        panel.add(Box.createVerticalStrut(10));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DB2025Team03_allGui());
    }
}

