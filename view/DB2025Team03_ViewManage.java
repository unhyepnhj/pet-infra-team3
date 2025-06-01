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

    public DB2025Team03_ViewManage() {
        setTitle("반려동물 관리 시스템");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manage = new DB2025Team03_Manage();

        setupUI();

        setVisible(true);
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        actionPanel = new JPanel(new FlowLayout());

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
        JOptionPane.showMessageDialog(this, "\uD83D\uDD27 반려동물 관리 화면은 기존대로 유지됩니다.");
    }

    private void showReservationList(int userId) {
        actionPanel.removeAll();
        actionPanel.setLayout(new BorderLayout());

        List<DB2025Team03_ModelReservation> reservations = manage.reservationController.searchByUserId(userId);
        String[] columnNames = {"예약 번호", "사용자 ID", "시설 ID", "예약 날짜", "서비스 종류", "슬롯 ID"};
        Object[][] data = new Object[reservations.size()][columnNames.length];

        for (int i = 0; i < reservations.size(); i++) {
            DB2025Team03_ModelReservation r = reservations.get(i);
            data[i][0] = r.getReservationId();
            data[i][1] = r.getUserId();
            data[i][2] = r.getFacilityId();
            data[i][3] = r.getDate();
            data[i][4] = r.getServiceType();
            data[i][5] = r.getSlotId();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        actionPanel.add(new JScrollPane(table), BorderLayout.CENTER);

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
