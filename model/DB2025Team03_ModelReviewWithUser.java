package model;

import java.sql.Date;

public class DB2025Team03_ModelReviewWithUser {
    private String name;
    private int rating;
    private String content;
    private Date date;

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
