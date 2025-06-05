package model;

import java.sql.Date;

/*
 * ControllerReview 클래스에서 사용자별 리뷰 검색하기 위해 사용할 getter, setter
 */

public class DB2025Team03_ModelReviewWithUser {
    private String name;
    private int rating;
    private String content;
    private Date date;
    
    /**
     * @param name		: 사용자 이름
     * @param rating	: 평점
     * @param content	: 리뷰 내용
     * @param date		: 이용 일자
     */

    public DB2025Team03_ModelReviewWithUser(String name, int rating, String content, Date date) {
        this.name    = name;
        this.rating  = rating;
        this.content = content;
        this.date    = date;
    }

    // 필요한 경우 getter, setter, toString() 등 추가
    public String getName() { return name; }
    public int    getRating() { return rating; }
    public String getContent() { return content; }
    public Date   getDate() { return date; }
}
