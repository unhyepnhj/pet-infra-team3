package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.DB2025Team03_ControllerReservationSlot;
import model.DB2025Team03_ModelPet;
import model.DB2025Team03_ModelReservation;
import model.DB2025Team03_ModelReservationSlot;

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
    
    public DB2025Team03_ViewManage(int userId) {	// 6/2 수정: allGui에서 입력받은 ID 사용하는 생성자 추가
        setTitle("반려동물 관리 시스템");
        setSize(700, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);	// 6/2 수정: EXIT -> DISPOSE
        setLocationRelativeTo(null);
        this.userId = userId; // 추가
        manage = new DB2025Team03_Manage();

        setupUI();
        
        setAlwaysOnTop(true);  
        setVisible(true);
        toFront();
    }

    public void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        actionPanel = new JPanel(new FlowLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
//        userIdField = new JTextField(10);
//        JButton loadBtn = new JButton("확인");
//        topPanel.add(new JLabel("사용자 ID:"));
//        topPanel.add(userIdField);
//        topPanel.add(loadBtn);
//
//        loadBtn.addActionListener(e -> {
//            String input = userIdField.getText().trim();
//            if (!input.matches("\\d+")) {
//                JOptionPane.showMessageDialog(this, "ID는 숫자로 입력하세요.");
//                return;
//            }
//            int userId = Integer.parseInt(input);
//            showUserOptions(userId);
//        });

     // 6/2 수정: ID 입력 -> 로그인된 사용자 ID 표시하는 것으로 변경
        JLabel userLabel = new JLabel("현재 로그인 ID: " + userId);
        userLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        topPanel.add(userLabel);
        showUserOptions(userId);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

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
        
        // System.out.println("클릭됨");	// 디버깅용
        petManageBtn.addActionListener(e -> showPetManagement(userId));
        reservationViewBtn.addActionListener(e -> showReservationList(userId));
        reservationRegisterBtn.addActionListener(e -> showReservationForm(userId));

        actionPanel.add(petManageBtn);
        actionPanel.add(reservationViewBtn);
        actionPanel.add(reservationRegisterBtn);

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showPetManagement(int userId) {
        // actionPanel.removeAll();
    	// actionPanel.setLayout(new BorderLayout());
    	mainPanel.remove(actionPanel);	// 6/2 수정
    	actionPanel = new JPanel(new BorderLayout());

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
        
        mainPanel.add(actionPanel, BorderLayout.CENTER);	// 6/2 추가
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
             /*  !!!6/1 수정코드!!!*/
            "예약 번호",
            "사용자 ID",
            "시설 ID", 
            "예약 날짜",
            "예약 유형",
            "리뷰작성" 
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
            data[i][5]= "리뷰작성";
        }

        // 모델 및 테이블 생성
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
            	  /*  !!!6/1 수정코드!!!*/
            	// '리뷰작성'만 클릭 가능하도록 
                return column==5;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(30);

        
        /*  !!!6/1 수정코드!!!*/
        //!! 리뷰 작성 버튼 추가
        
        
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
        JTextField reservationIdField = new JTextField(10);
        JComboBox<DB2025Team03_ModelReservationSlot> slotComboBox = new JComboBox<>();

        // 슬롯 불러오는 버튼
        JButton loadSlotBtn = new JButton("예약 시간 불러오기");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("시설 ID:"));
        panel.add(facilityIdField);
        panel.add(new JLabel("예약 날짜 (yyyy-mm-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("예약 ID:"));
        panel.add(reservationIdField);
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
                int reservationId = Integer.parseInt(reservationIdField.getText());
                int facilityId = Integer.parseInt(facilityIdField.getText());
                String date = dateField.getText();
                DB2025Team03_ModelReservationSlot selectedSlot =
                        (DB2025Team03_ModelReservationSlot) slotComboBox.getSelectedItem();
                if (selectedSlot == null) {
                    JOptionPane.showMessageDialog(this, "예약 시간을 선택하세요.");
                    return;
                }

                int slotId = selectedSlot.getSlotId();

                manage.registerReservation(reservationId, userId, facilityId, date, "", slotId);

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
