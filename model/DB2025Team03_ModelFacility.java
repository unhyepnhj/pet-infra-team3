package model;

/*
 * ControllerFacility 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelFacility {
    private int facilityId;
    private String name;
    private String address;
    private String category;
    private String openingHours;

    /**
     * @param facilityId : 시설 ID
     * @param name     	 : 시설명
     * @param address    : 시설 주소
     * @param category   : 시설 유형(병원, 미용실, etc.)
     * @param openingHours : 시설 오픈 시간
     */
    
    public DB2025Team03_ModelFacility(int facilityId, String name, String address,
                                      String category, String openingHours) {
        this.facilityId = facilityId;
        this.name = name;
        this.address = address;
        this.category = category;
        this.openingHours = openingHours;
    }

    // Getter, Setter
    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    @Override
    public String toString() {
        return "DB2025Team03_ModelFacility{" +
               "facilityId=" + facilityId +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", category='" + category + '\'' +
               ", openingHours='" + openingHours + '\'' +
               '}';
    }
}
