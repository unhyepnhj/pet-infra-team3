package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.DB2025Team03_ModelPet;
import model.DB2025Team03_ModelReservation;

public class DB2025Team03_ViewManage extends JFrame {
    private JTextField userIdField;
    private JButton petManageBtn, reservationViewBtn;
    private JPanel mainPanel, actionPanel;
    private DB2025Team03_Manage manage;

    public DB2025Team03_ViewManage() {
        setTitle("반려동물 관리 시스템");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manage = new DB2025Team03_Manage();

        setupUI();

        setVisible(true);
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        actionPanel = new JPanel(new FlowLayout());
        
        // ID 입력(로그인 창??)
        JPanel topPanel = new JPanel(new FlowLayout());
        userIdField = new JTextField(10);
        JButton loadBtn = new JButton("확인");
        topPanel.add(new JLabel("사용자 ID:"));
        topPanel.add(userIdField);
        topPanel.add(loadBtn);

        loadBtn.addActionListener(e -> {
            String input = userIdField.getText().trim();
            if (!input.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "ID는 숫자로 입력하세요.");
                return;
            }
            int userId = Integer.parseInt(input);
            showUserOptions(userId);
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void showUserOptions(int userId) {	// 반려동물 관리 or 조회 선택 화면
        actionPanel.removeAll();
        actionPanel.setLayout(new FlowLayout());

        petManageBtn = new JButton("반려동물 관리");
        reservationViewBtn = new JButton("예약 내역 조회");

        petManageBtn.addActionListener(e -> showPetManagement(userId));	// 관리 선택하면 showPetaManagement
        reservationViewBtn.addActionListener(e -> showReservationList(userId));	// 예약 선택하면 showReservationList

        actionPanel.add(petManageBtn);
        actionPanel.add(reservationViewBtn);

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showPetManagement(int userId) {
        actionPanel.removeAll();
        actionPanel.setLayout(new BorderLayout());

        List<DB2025Team03_ModelPet> pets = manage.petController.searchByUserId(userId);

        // JTable에 넣을 데이터 생성
        String[] columnNames = {"반려동물 ID", "사용자 ID", "이름", "나이", "종", "수정", "삭제"};
        Object[][] data = new Object[pets.size()][7];

        for (int i = 0; i < pets.size(); i++) {
            DB2025Team03_ModelPet pet = pets.get(i);
            data[i][0] = pet.getPetId();
            data[i][1] = pet.getUserId();
            data[i][2] = pet.getName();
            data[i][3] = pet.getAge();
            data[i][4] = pet.getSpecies();
            data[i][5] = "수정";
            data[i][6] = "삭제";
        }

        // 모델과 테이블 생성
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // 수정, 삭제 버튼만 클릭 가능
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(30);

        // 버튼 기능 처리
        table.getColumn("수정").setCellRenderer(new ButtonRenderer());
        table.getColumn("삭제").setCellRenderer(new ButtonRenderer());
        table.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), "수정", (row) -> {
            DB2025Team03_ModelPet pet = pets.get(row);
            String newName = JOptionPane.showInputDialog("새 이름 입력:", pet.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                manage.updatePetName(userId, pet.getPetId(), newName);
                showPetManagement(userId); // 갱신
            }
        }));
        table.getColumn("삭제").setCellEditor(new ButtonEditor(new JCheckBox(), "삭제", (row) -> {
            DB2025Team03_ModelPet pet = pets.get(row);
            int result = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                manage.deletePet(userId, pet.getPetId());
                showPetManagement(userId); // 갱신
            }
        }));

        JScrollPane scrollPane = new JScrollPane(table);
        actionPanel.add(scrollPane, BorderLayout.CENTER);

        // 상단 우측 등록/뒤로가기 버튼
        JButton registerBtn = new JButton("반려동물 등록");
        registerBtn.addActionListener(e -> {
            JTextField nameField = new JTextField(10);
            JTextField ageField = new JTextField(5);
            JTextField speciesField = new JTextField(10);
            JPanel regPanel = new JPanel(new GridLayout(3, 2));	// 등록 팝업
            regPanel.add(new JLabel("이름:"));
            regPanel.add(nameField);
            regPanel.add(new JLabel("나이:"));
            regPanel.add(ageField);
            regPanel.add(new JLabel("종:"));
            regPanel.add(speciesField);

            int result = JOptionPane.showConfirmDialog(this, regPanel, "반려동물 등록", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int age = Integer.parseInt(ageField.getText());
                    manage.registerPet(userId, nameField.getText(), age, speciesField.getText());	// Manage.registerPet() 메소드 사용해 반려동물 등록
                    showPetManagement(userId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "나이는 숫자로 입력해주세요.");
                }
            }
        });
        
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));	// 등록 버튼 - 우측 상단
        topRightPanel.add(registerBtn);
        actionPanel.add(topRightPanel, BorderLayout.NORTH);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));	// 뒤로가기 버튼 - 좌측 하단
        JButton backBtn = new JButton("뒤로가기");
        backBtn.addActionListener(e -> showUserOptions(userId));
        bottomPanel.add(backBtn);
        actionPanel.add(bottomPanel, BorderLayout.SOUTH);

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showReservationList(int userId) {
        actionPanel.removeAll();
        actionPanel.setLayout(new BorderLayout());

        // 사용자별 예약 내역 조회
        List<DB2025Team03_ModelReservation> reservations = manage.reservationController.searchByUserId(userId);
        
        // JTable 컬럼 정의 (필요에 따라 수정)
        String[] columnNames = {
            "예약 번호",
            "사용자 ID",
            "반려동물 ID",
            "예약 날짜",
            "예약 시간"
        };
        Object[][] data = new Object[reservations.size()][columnNames.length];

        // 데이터 채우기
        for (int i = 0; i < reservations.size(); i++) {
            DB2025Team03_ModelReservation res = reservations.get(i);
            data[i][0] = res.getReservationId();
            data[i][1] = res.getUserId();
            data[i][2] = res.getFacilityId();
            data[i][3] = res.getDate();   // 실제 getter에 맞게 조정
            data[i][4] = res.getServiceType();
        }

        // 모델 및 테이블 생성
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false; // 전체 비활성화 (조회만 가능)
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        actionPanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 뒤로가기 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("뒤로가기");
        backBtn.addActionListener(e -> showUserOptions(userId));
        bottomPanel.add(backBtn);
        actionPanel.add(bottomPanel, BorderLayout.SOUTH);

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DB2025Team03_ViewManage::new);
    }
}
