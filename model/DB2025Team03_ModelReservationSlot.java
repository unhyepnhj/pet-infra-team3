package model;

/*
 * ControllerReservationSlot 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelReservationSlot {
    private int slotId;
    private int facilityId;
    private String slotTime;
    private boolean isReserved;
    
    /**
     * 
     * @param slotId		: slot ID
     * @param facilityId	: 시설 ID
     * @param slotTime		: slot의 시간대
     * @param isReserved	: 예약 여부 표시
     */

    public DB2025Team03_ModelReservationSlot(int slotId, int facilityId, String slotTime, boolean isReserved) {
        this.slotId = slotId;
        this.facilityId = facilityId;
        this.slotTime = slotTime;
        this.isReserved = isReserved;
    }

    public int getSlotId() { return slotId; }
    public int getFacilityId() { return facilityId; }
    public String getSlotTime() { return slotTime; }
    public boolean isReserved() { return isReserved; }

    @Override
    public String toString() {
        return slotTime;
    }
}
