package model;

/*
 * ControllerFavorite 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelFavorite {
    private int userId;
    private int facilityId;

    /**
     * @param userId      : 유저 ID
     * @param facilityId  : 시설 ID
     */
    
    public DB2025Team03_ModelFavorite(int userId, int facilityId) {
        this.userId = userId;
        this.facilityId = facilityId;
    }

    // Getter, Setter
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

    @Override
    public String toString() {
        return "DB2025Team03_ModelFavorite{" +
               "userId=" + userId +
               ", facilityId=" + facilityId +
               '}';
    }
}
