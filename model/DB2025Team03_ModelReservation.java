package model;

/*
 * ControllerReservation 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelReservation {
    private int reservationId;
    private int userId;
    private int facilityId;
    private String date;
    private String serviceType;
    private int slotId;
    
    /**
     * 
     * @param reservationId	: 예약 번호
     * @param userId		: 유저 ID
     * @param facilityId	: 시설 ID
     * @param date			: 예약 일자
     * @param serviceType	: 예약한 서비스 유형
     * @param slotId		: 예약 slot
     */
    
    public DB2025Team03_ModelReservation(int reservationId, int userId, int facilityId, String date, String serviceType, int slotId) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.date = date;
        this.serviceType = serviceType;
        this.slotId = slotId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public int getSlotId() {
        return slotId;
    }

    @Override
    public String toString() {
        return "DB2025Team03_ModelReservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", facilityId=" + facilityId +
                ", date='" + date + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", slotId=" + slotId +
                '}';
    }
}



