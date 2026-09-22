package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.ReviewDAO;
import com.fashionstore.model.Review;
import com.fashionstore.util.DBConnection;

public class ReviewDAOImpl implements ReviewDAO {

    public ReviewDAOImpl() {
        // Connection management handled per-method
    }

    private Connection openConnection() {
        return DBConnection.getConnection();
    }

    @Override
    public boolean addReview(Review review) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return reviews;

            String query =
                    "SELECT r.*, u.name AS user_name " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.product_id=? " +
                    "ORDER BY r.created_at DESC";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review review = new Review();

                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setRating(rs.getInt("rating"));
                review.setReviewText(rs.getString("review_text"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                review.setUserName(rs.getString("user_name"));

                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return reviews;
    }

    @Override
    public double getAverageRating(int productId) {
        double rating = 0;
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return rating;

            String query = "SELECT AVG(rating) AS average_rating FROM reviews WHERE product_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rating = rs.getDouble("average_rating");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return rating;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "DELETE FROM reviews WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, reviewId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }
}