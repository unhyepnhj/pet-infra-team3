package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DB2025Team03_ModelReview;
import model.DB2025Team03_ModelReviewWithUser;

/*
 * DB2025Team03_Review 테이블에 접근하여 리뷰 관리
 */

public class DB2025Team03_ControllerReview {
    private Connection conn;

    public DB2025Team03_ControllerReview() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB2025Team03",
                    "root",
                    "root"
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // INSERT
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

    // DELETE: 리뷰 아이디=reviewId인 리뷰 삭제
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

    // UPDATE
    // 리뷰 아이디=reviewId인 리뷰의 내용을 newContent로 변경
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

    // 리뷰 아이디=reviewId인 리뷰의 평점을 newRating으로 변경
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

    // 검색
    // 유저 아이디=userId인 유저가 작성한 리뷰 조회
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

    // 6/2 수정: 시설 ID로 시설별 리뷰 조회
    public List<DB2025Team03_ModelReviewWithUser> getReviewsByFacilityIdDB2025Team03(int facilityId) {
        String sql = "SELECT U.name, R.rating, R.content, R.date " +
                     "FROM DB2025_Review R " +
                     "JOIN DB2025_User U ON R.user_id = U.user_id " +
                     "WHERE R.facility_id = ?";
        List<DB2025Team03_ModelReviewWithUser> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facilityId);
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

    // 사용자 이름으로 해당 사용자가 작성한 리뷰 조회
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
