package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controller.DB2025Team03_ControllerReservationSlot;
import controller.DB2025Team03_ControllerUserActivity;
import model.DB2025Team03_ModelPet;
import model.DB2025Team03_ModelReservation;
import model.DB2025Team03_ModelReservationSlot;
import model.DB2025Team03_ModelUserActivity;

/*
 * DB2025Team03_Manage 클래스에 구현된 내 정보 관리 기능을 유저 인터페이스에 구현
 */

public class DB2025Team03_ViewManage extends JFrame {
    private JTextField userIdField;
    private JPanel mainPanel, actionPanel;
    private DB2025Team03_Manage manage;
    private int userId;	// 6/2 수정: ID 전역 변수로 변경

    public DB2025Team03_ViewManage() {
        setTitle("반려동물 관리 시스템");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manage = new DB2025Team03_Manage();

        setupUI();

        setVisible(true);
    }
    
    // 6/2 수정: 기본 생성자 삭제하고 allGui에서 입력받은 uid 사용하는 생성자 추가
    public DB2025Team03_ViewManage(int userId) {
        setTitle("반려동물 관리 시스템");
        setSize(700, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);	// 6/2 수정: EXIT -> DISPOSE
        setLocationRelativeTo(null);
        this.userId = userId;
        manage = new DB2025Team03_Manage();

        setupUI();
        
        // setAlwaysOnTop(true);  
        setVisible(true);
        toFront();
    }

    public void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        actionPanel = new JPanel(new FlowLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        // 6/2 수정: 매번 ID 입력하는 것에서 앞서 로그인한 사용자 ID 표시하는 것으로 변경
        JLabel userLabel = new JLabel("현재 로그인 ID: " + userId);
        userLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        topPanel.add(userLabel);
        showUserOptions(userId);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    // "내 정보 관리"기능 메인 화면: 반려동물 관리/예약 조회/예약 등록 옵션 버튼 표시
    private void showUserOptions(int userId) {
        actionPanel.removeAll();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        JButton petManageBtn = new JButton("반려동물 관리");
        JButton reservationViewBtn = new JButton("예약 조회");
        JButton reservationRegisterBtn = new JButton("예약 등록");

        Dimension btnSize = new Dimension(150, 40);
        petManageBtn.setPreferredSize(btnSize);
        reservationViewBtn.setPreferredSize(btnSize);
        reservationRegisterBtn.setPreferredSize(btnSize);
        
        // 각 버튼 클릭 시 이벤트
        petManageBtn.addActionListener(e -> showPetManagement(userId));
        reservationViewBtn.addActionListener(e -> showReservationList(userId));
        reservationRegisterBtn.addActionListener(e -> showReservationForm(userId));

        actionPanel.add(petManageBtn);
        actionPanel.add(reservationViewBtn);
        actionPanel.add(reservationRegisterBtn);

        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
// "반려동물 관리" 버튼 클릭될 경우
    private void showPetManagement(int userId) {
    	mainPanel.remove(actionPanel);
    	actionPanel = new JPanel(new BorderLayout());

        List<DB2025Team03_ModelPet> pets = manage.petController.searchByUserId(userId);
        
        // JTable로 반려동물 리스트 출력
        // JTable 정의
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
        // "수정" 클릭했을 경우
        table.getColumn("수정").setCellEditor(new ButtonEditor(new JCheckBox(), "수정", (row) -> {
        	DB2025Team03_ModelPet pet = pets.get(row);
            String newName = JOptionPane.showInputDialog("새 이름 입력:", pet.getName());
            // 잘못된 입력 제외하고 업데이트
            if (newName != null && !newName.trim().isEmpty()) {
                manage.updatePetName(userId, pet.getPetId(), newName);
                showPetManagement(userId);
            }
        }));
        // "삭제" 클릭했을 경우 해당 행의 반려동물 삭제
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

        // 사용자 편의성 위한 상단 우측 등록/뒤로가기 버튼
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
        
        mainPanel.add(actionPanel, BorderLayout.CENTER);	// 6/2 추가
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
// "예약 조회" 버튼 클릭될 경우
    // 전체 예약 내역 리스트 출력
    private void showReservationList(int userId) {
        actionPanel.removeAll();
        actionPanel.setLayout(new BorderLayout());
        
        DB2025Team03_ControllerUserActivity activityController = new DB2025Team03_ControllerUserActivity();
        DB2025Team03_ModelUserActivity activity = activityController.getUserActivitySummary(userId);
        if (activity != null) {
            String summaryText = String.format("총 예약: %d건, 총 리뷰: %d건",
                activity.getTotalReservations(), activity.getTotalReviews());
            JLabel summaryLabel = new JLabel(summaryText);
            summaryLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
            actionPanel.add(summaryLabel, BorderLayout.NORTH);
        }
        activityController.close();

        // 사용자별 예약 내역 조회
        List<DB2025Team03_ModelReservation> reservations = manage.reservationController.searchByUserId(userId);
        
        // 6/3 추가: slot 기능
        DB2025Team03_ControllerReservationSlot slotController = new DB2025Team03_ControllerReservationSlot();
        
        // JTable 컬럼 정의 (필요에 따라 수정)
        String[] columnNames = {
            "예약 번호",
            "시설 ID", 
            "예약 날짜",
            "예약 시간",/*06-03 수정함*/
            "예약 취소",/*06-03 수정함*/
            "리뷰작성" 
        };
        Object[][] data = new Object[reservations.size()][columnNames.length];

        // 데이터 채우기
        for (int i = 0; i < reservations.size(); i++) {
            DB2025Team03_ModelReservation res = reservations.get(i);
            data[i][0] = res.getReservationId();
            data[i][1] = res.getFacilityId();
            data[i][2] = res.getDate();   // 실제 getter에 맞게 조정
            String timeRange = slotController.getTimeRangeBySlotId(res.getSlotId());
            data[i][3] = timeRange;
            data[i][4] = "예약 취소";
            data[i][5]= "리뷰 작성";
        }

     // 모델 및 테이블 생성
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 예약 취소(4번) & 리뷰 작성(5번) 둘 다 클릭 가능하게 함
                return column == 4 || column == 5;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(30);

        /*06-03 수정 코드 취소 기능*/
        table.getColumn("예약 취소").setCellRenderer(new ButtonRenderer());
        table.getColumn("예약 취소").setCellEditor(new ButtonEditor(new JCheckBox(), "예약 취소", (row) -> {
            DB2025Team03_ModelReservation res = reservations.get(row);
            int reservationId = res.getReservationId();
            int slotId = res.getSlotId();

            int result = JOptionPane.showConfirmDialog(this, "정말 예약을 취소하시겠습니까?", "예약 취소 확인", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                manage.reservationController.deleteReservation(reservationId);
                manage.slotController.unmarkSlotAsReserved(slotId);
                JOptionPane.showMessageDialog(this, "예약이 취소되었습니다.");
                showReservationList(userId); // 갱신
            }
        }));
        
        // 리뷰 작성 버튼 추가 및 클릭 이벤트 구현
        table.getColumn("리뷰작성").setCellRenderer(new ButtonRenderer());
        table.getColumn("리뷰작성").setCellEditor(new ButtonEditor(new JCheckBox(), "리뷰작성", (row) -> {
            DB2025Team03_ModelReservation res = reservations.get(row);
            int facilityId = res.getFacilityId();
            // 리뷰 창 열기 
            new DB2025Team03_ViewReview(userId, facilityId); 
        }));
        
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

    private void showReservationForm(int userId) {
        JTextField facilityIdField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField serviceTypeField = new JTextField(10);
        JComboBox<DB2025Team03_ModelReservationSlot> slotComboBox = new JComboBox<>();
        
        // 슬롯 불러오는 버튼
        JButton loadSlotBtn = new JButton("예약 시간 불러오기");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("시설 ID:"));
        panel.add(facilityIdField);
        
        panel.add(new JLabel("예약 날짜 (yyyy-mm-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("예약 시간:"));
        panel.add(slotComboBox);
        panel.add(new JLabel(""));
        panel.add(loadSlotBtn);

        // 슬롯 불러오기 버튼 클릭 시 동작
        loadSlotBtn.addActionListener(e -> {
            try {
                int facilityId = Integer.parseInt(facilityIdField.getText().trim());
                String date = dateField.getText().trim();

                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "날짜 형식은 yyyy-mm-dd입니다.");
                    return;
                }

                DB2025Team03_ControllerReservationSlot slotController = new DB2025Team03_ControllerReservationSlot();
                List<DB2025Team03_ModelReservationSlot> slots = slotController.getAvailableSlots(facilityId, date);

                slotComboBox.removeAllItems();
                for (DB2025Team03_ModelReservationSlot s : slots) {
                    slotComboBox.addItem(s);
                }

                if (slots.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "해당 날짜에 예약 가능한 시간이 없습니다.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력 오류: " + ex.getMessage());
            }
        });

        int result = JOptionPane.showConfirmDialog(this, panel, "예약 등록", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int facilityId = Integer.parseInt(facilityIdField.getText());
                String date = dateField.getText();
                DB2025Team03_ModelReservationSlot selectedSlot =
                        (DB2025Team03_ModelReservationSlot) slotComboBox.getSelectedItem();
                if (selectedSlot == null) {
                    JOptionPane.showMessageDialog(this, "예약 시간을 선택하세요.");
                    return;
                }

                int slotId = selectedSlot.getSlotId();
                
                String serviceType = serviceTypeField.getText(); 
                manage.registerReservation(userId, facilityId, date, serviceType, slotId);

                // 슬롯 사용 처리
                DB2025Team03_ControllerReservationSlot slotController = new DB2025Team03_ControllerReservationSlot();
                slotController.markSlotAsReserved(slotId);

                JOptionPane.showMessageDialog(this, "✅ 예약이 완료되었습니다.");
                showReservationList(userId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력 오류: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DB2025Team03_ViewManage::new);
    }
}
