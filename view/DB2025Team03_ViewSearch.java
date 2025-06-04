package view;

import controller.DB2025Team03_ControllerFacility;
import model.DB2025Team03_ModelFacility;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DB2025Team03_ViewSearch {
    private DB2025Team03_ControllerFacility facCtrl;

    public DB2025Team03_ViewSearch() throws SQLException {
        // ① 컨트롤러 초기화하면서 DB 연결
        facCtrl = new DB2025Team03_ControllerFacility();
    }
    
    public void showMenu() {
        while (true) {
            // ② 사용자에게 메뉴 선택 받기
            String sel = JOptionPane.showInputDialog(null,
                "===== 검색 메뉴 =====\n" +
                "1) 이름으로 시설 검색\n" +
                "2) 주소(지역)로 시설 검색\n" +
                "3) 유형으로 시설 검색 (ex.미용실, 병원)\n" +
                "4)	방문 예정 시간 운영중인 시설 검색\n" +
                "5) 평균 평점 이상 시설 검색(1~5)\n" +
                "0) 종료\n\n" +
                "선택(0~5)> "
            );
            // 취소나 창 닫기 → 종료
            if (sel == null) return;
            int menu;
            try {
                menu = Integer.parseInt(sel.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                  null, "1~5 또는 0을 입력해주세요.",
                  "입력 오류", JOptionPane.WARNING_MESSAGE
                );
                continue;
            }

            if (menu == 0) return;

            // ③ 각 메뉴별로 파라미터 받고 컨트롤러 호출
            List<DB2025Team03_ModelFacility> results;
            switch (menu) {
                case 1:
                    String name = JOptionPane.showInputDialog("검색할 이름 키워드>");
                    if (name == null) continue;  // 취소 시 처음으로
                    results = facCtrl.searchByNameDB2025Team03(name.trim());
                    break;

                case 2:
                    String address = JOptionPane.showInputDialog("검색할 주소(지역) 키워드>");
                    if (address == null) continue;
                    results = facCtrl.searchByAddressDB2025Team03(address.trim());
                    break;

                case 3:
                    String category = JOptionPane.showInputDialog("검색할 유형(ex.미용실, 병원)>");
                    if (category == null) continue;
                    results = facCtrl.searchByCategoryDB2025Team03(category.trim());
                    break;

                case 4:
                    String opening_hours = JOptionPane.showInputDialog(
                      "기준 오픈 시간 입력 (예: 09:00)>");
                    if (opening_hours == null) continue;
                    results = facCtrl.searchByOpeningBeforeDB2025Team03(opening_hours.trim());
                    
                    //openingHours가 빈 값이 많아서 여기만 조건문 걸어줄게
                    if (results != null) {
                        results.removeIf(f ->
                            f.getOpeningHours() == null ||
                            f.getOpeningHours().trim().isEmpty()
                        );
                    }
                    break;

                case 5:
                    String rt = JOptionPane.showInputDialog("최소 평균 평점 입력>");
                    if (rt == null) continue;
                    double minRt;
                    try {
                        minRt = Double.parseDouble(rt.trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                          null, "평점은 숫자로 입력해주세요.",
                          "입력 오류", JOptionPane.WARNING_MESSAGE
                        );
                        continue;
                    }
                    results = facCtrl.searchByMinAvgRatingDB2025Team03(minRt);
                    break;

                default:
                    JOptionPane.showMessageDialog(
                      null, "1~5 또는 0을 입력해주세요.",
                      "잘못된 선택", JOptionPane.WARNING_MESSAGE
                    );
                    continue;
            }

            // ④ 결과 출력
            showResults(results);
        }
    }

    private void showResults(List<DB2025Team03_ModelFacility> results) {
    	JTextArea ta = new JTextArea(20, 60);
        ta.setEditable(false);
       
        if (results.isEmpty()) {
            ta.setText("조회된 시설이 없습니다.");
        } else {
            ta.append("ID\t이름\t주소\t유형\t오픈시간\n");
            for (DB2025Team03_ModelFacility f : results) {
                ta.append(String.format("%d\t%s\t%s\t%s\t%s\n",
                    f.getFacilityId(),
                    f.getName(),
                    f.getAddress(),
                    f.getCategory(),
                    f.getOpeningHours()
                ));
            }
        }

        JScrollPane scroll = new JScrollPane(ta);
        JOptionPane.showMessageDialog(
            null, scroll, "검색 결과",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DB2025Team03_ViewSearch().showMenu();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                  null, "초기화 실패:\n" + e.getMessage(),
                  "DB 오류", JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}