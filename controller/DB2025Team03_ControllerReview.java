package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DB2025Team03_ModelReview;

class DB2025Team03_ModelReview {
    private int reviewId;
    private int userId;
    private int facilityId;
    private int rating;
    private String content;
    private Date date;

    public DB2025Team03_ModelReview(int reviewId, int userId, int facilityId, int rating, String content, Date date) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.rating = rating;
        this.content = content;
        this.date = date;
    }

    // Getter
    public int getReviewId() { return reviewId; }
    public int getUserId() { return userId; }
    public int getFacilityId() { return facilityId; }
    public int getRating() { return rating; }
    public String getContent() { return content; }
    public Date getDate() { return date; }
}

class DB2025Team03_ModelReviewWithUser {
    private String userName;
    private int rating;
    private String content;
    private Date date;

    public DB2025Team03_ModelReviewWithUser(String userName, int rating, String content, Date date) {
        this.userName = userName;
        this.rating = rating;
        this.content = content;
        this.date = date;
    }

    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getContent() { return content; }
    public Date getDate() { return date; }
}

public class DB2025Team03_ControllerReview {
    private Connection conn;

    public DB2025Team03_ControllerReview() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB2025Team03?serverTimezone=UTC",
                "root",
                "DB2025Team03"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 리뷰 추가
    public void insertReviewDB2025Team03(int userId, int facilityId, int rating, String content, Date date) {
        String sql = "INSERT INTO DB2025_Review (user_id, facility_id, rating, content, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, facilityId);
            ps.setInt(3, rating);
            ps.setString(4, content);
            ps.setDate(5, new Date(System.currentTimeMillis())); // 오늘 날짜 자동 삽입
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 리뷰 삭제
    public int deleteReviewDB2025Team03(int reviewId) {
        String sql = "DELETE FROM DB2025_Review WHERE review_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 리뷰 내용 수정
    public void updateReviewContentDB2025Team03(int reviewId, String newContent) {
        String sql = "UPDATE DB2025_Review SET content = ? WHERE review_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newContent);
            ps.setInt(2, reviewId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 리뷰 평점 수정
    public void updateReviewRatingDB2025Team03(int reviewId, int newRating) {
        String sql = "UPDATE DB2025_Review SET rating = ? WHERE review_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newRating);
            ps.setInt(2, reviewId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //특정 사용자 리뷰 조회회
    public List<DB2025Team03_ModelReview> getReviewsByUserDB2025Team03(int userId) {
        String sql = "SELECT * FROM DB2025_Review WHERE user_id = ?";
        List<DB2025Team03_ModelReview> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 리뷰 ID로 조회회
    public DB2025Team03_ModelReview getReviewByIdDB2025Team03(int reviewId) {
        String sql = "SELECT * FROM DB2025_Review WHERE review_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //사용자 이름으로 리뷰 검색
    public List<DB2025Team03_ModelReviewWithUser> searchReviewsByUserNameDB2025Team03(String name) {
        String sql = "SELECT U.name, R.rating, R.content, R.date " +
                     "FROM DB2025_Review R " +
                     "JOIN DB2025_User U ON R.user_id = U.user_id " +
                     "WHERE U.name = ?";
        List<DB2025Team03_ModelReviewWithUser> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DB2025Team03_ModelReviewWithUser(
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getString("content"),
                        rs.getDate("date")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ResultSet → Model 변환
    private DB2025Team03_ModelReview mapRow(ResultSet rs) throws SQLException {
        return new DB2025Team03_ModelReview(
            rs.getInt("review_id"),
            rs.getInt("user_id"),
            rs.getInt("facility_id"),
            rs.getInt("rating"),
            rs.getString("content"),
            rs.getDate("date")
        );
    }
}
