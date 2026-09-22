package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.WishlistDAO;
import com.fashionstore.model.Product;
import com.fashionstore.util.DBConnection;

public class WishlistDAOImpl implements WishlistDAO {

    public WishlistDAOImpl() {
        // Connection management handled per-method
    }

    private Connection openConnection() {
        return DBConnection.getConnection();
    }

    @Override
    public boolean addToWishlist(int userId, int productId) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] WishlistDAOImpl.addToWishlist: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public boolean removeFromWishlist(int userId, int productId) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "DELETE FROM wishlist WHERE user_id=? AND product_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] WishlistDAOImpl.removeFromWishlist: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public List<Product> getWishlistProducts(int userId) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return products;

            String query =
                    "SELECT p.* FROM products p " +
                    "JOIN wishlist w ON p.product_id = w.product_id " +
                    "WHERE w.user_id = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();

                product.setId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));
                product.setCreatedAt(rs.getTimestamp("created_at"));

                products.add(product);
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] WishlistDAOImpl.getWishlistProducts: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return products;
    }

    @Override
    public boolean isProductInWishlist(int userId, int productId) {
        Connection conn = null;
        boolean exists = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "SELECT * FROM wishlist WHERE user_id=? AND product_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] WishlistDAOImpl.isProductInWishlist: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return exists;
    }
}