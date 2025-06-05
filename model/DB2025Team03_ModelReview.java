package model;

import java.sql.Date;

/*
 * ControllerReview 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelReview {
    private int reviewId;
    private int userId;
    private int facilityId;
    private int rating;
    private String content;
    private Date date;

    /**
     * @param reviewId 리뷰 ID
     * @param userId 사용자 ID
     * @param facilityId 시설 ID
     * @param rating 평점
     * @param content 내용
     * @param date 작성일자
     */
    public DB2025Team03_ModelReview(int reviewId, int userId, int facilityId, int rating, String content, Date date) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.rating = rating;
        this.content = content;
        this.date = date;
    }

    // Getter, Setter
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DB2025Team03_ModelReview{" +
               "reviewId=" + reviewId +
               ", userId=" + userId +
               ", facilityId=" + facilityId +
               ", rating=" + rating +
               ", content='" + content + '\'' +
               ", date=" + date +
               '}';
    }
}
