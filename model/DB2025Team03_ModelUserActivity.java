package model;

/*
 * ControllerUserActivity 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelUserActivity {
    private int userId;
    private String name;
    private int totalReservations;
    private int totalReviews;
    
    /**
     * @param userId	: 사용자 ID
     * @param name		: 사용자 이름
     * @param totalReservations	: 총 예약 수
     * @param totalReviews		: 작성한 리뷰 수
     */

    public DB2025Team03_ModelUserActivity(int userId, String name, int totalReservations, int totalReviews) {
        this.userId = userId;
        this.name = name;
        this.totalReservations = totalReservations;
        this.totalReviews = totalReviews;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public int getTotalReservations() { return totalReservations; }
    public int getTotalReviews() { return totalReviews; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setTotalReservations(int totalReservations) { this.totalReservations = totalReservations; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
}
