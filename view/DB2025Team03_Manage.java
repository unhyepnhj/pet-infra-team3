package view;

import controller.DB2025Team03_ControllerPet;
import controller.DB2025Team03_ControllerReservation;
import model.DB2025Team03_ModelPet;
import model.DB2025Team03_ModelReservation;

import java.util.List;
import controller.DB2025Team03_ControllerReservationSlot;

/*
 * 관리 기능
 * 1) 반려동물 관리
 * 2) 예약 내역 관리
 */

public class DB2025Team03_Manage {
    DB2025Team03_ControllerPet petController;
    DB2025Team03_ControllerReservation reservationController;
    DB2025Team03_ControllerReservationSlot slotController;

    public DB2025Team03_Manage() {
        petController = new DB2025Team03_ControllerPet();
        reservationController = new DB2025Team03_ControllerReservation();
        slotController = new DB2025Team03_ControllerReservationSlot();
    }
    
// 1) userId별 반려동물 관리(등록, 조회, 수정, 삭제)
    
    // 반려동물 등록
    public void registerPet(int userId, String name, int age, String species) {
        petController.insertPet2(userId, name, age, species);
        System.out.println(name+"이(가) 등록되었습니다.");
    }

    // 반려동물 조회
    public void showPets(int userId) {
        System.out.println("==== 사용자 " + userId + "의 반려동물 목록 ====");
        List<DB2025Team03_ModelPet> pets = petController.searchByUserId(userId);
        if (pets.isEmpty()) {
            System.out.println("등록된 반려동물이 없습니다.");
        } else {
            for (DB2025Team03_ModelPet pet : pets) {
                System.out.println(pet);
            }
        }
    }
    
    // 반려동물 이름 수정
    public void updatePetName(int userId, int petId, String newName) {
    	petController.updatePetName(userId, petId, newName);
    }
    
    // 반려동물 삭제
    public void deletePet(int userId, int petId) {
    	petController.deletePet(userId, petId);
    }
    
// 2) userId별 예약 관리(예약 내역 조회)
    
    public void showReservations(int userId) {
    	System.out.println("\n==== 사용자 " + userId + "의 예약 내역 ====");
        List<DB2025Team03_ModelReservation> reservations = reservationController.searchByUserId(userId);  // ← 변경됨
        
        System.out.println("예약 개수: " + reservations.size());
        
        if (reservations.isEmpty()) {
            System.out.println("예약 내역이 없습니다.");
        } else {
            for (DB2025Team03_ModelReservation res : reservations) {
                System.out.println(res);
            }
        }
    }
    
 //예약 등록
    public void registerReservation(int reservationId, int userId, int facilityId, String date, String serviceType, int slotId) {
        reservationController.insertReservation(reservationId, userId, facilityId, date, serviceType, slotId);
        slotController.markSlotAsReserved(slotId); //슬롯 상태 업데이트
        System.out.println("✅ 예약이 완료되었습니다.");
    }
}
