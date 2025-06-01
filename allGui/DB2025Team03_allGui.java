package allGui;

import view.DB2025Team03_ViewSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DB2025Team03_allGui {

    private JFrame   frame;
    private JPanel   panel;
    private JButton  btnFacility;
    private JButton  btnFavorite;
    private JButton  btnPet;
    private JButton  btnReservation;
    private JButton  btnReview;
    private JButton  btnUser;

    public DB2025Team03_allGui() {
        initGui();
    }

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
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));

        // 버튼 생성
        btnFacility     = makeButton("1) 시설 검색");
        btnFavorite     = makeButton("2) 즐겨찾기 검색");
        btnPet          = makeButton("3) 반려동물 검색");
        btnReservation  = makeButton("4) 예약 검색");
        btnReview       = makeButton("5) 리뷰 검색");
        btnUser         = makeButton("6) 사용자 검색");

        panel.add(btnFacility);
        panel.add(btnFavorite);
        panel.add(btnPet);
        panel.add(btnReservation);
        panel.add(btnReview);
        panel.add(btnUser);

        // 버튼 클릭 이벤트 연결
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
            frame.setVisible(false);
        });

        // TODO: 나머지 버튼도 뷰 클래스에 맞춰 같은 패턴으로 연결하세요.
//        btnFavorite.addActionListener(...);
//        btnPet.addActionListener(...);
//        btnReservation.addActionListener(...);
//        btnReview.addActionListener(...);
//        btnUser.addActionListener(...);

        frame.setVisible(true);
    }

    // 공통 버튼 생성 헬퍼
    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(500, 50));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(Box.createVerticalStrut(10));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DB2025Team03_allGui());
    }
}

