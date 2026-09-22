// WishlistDAOImpl.java

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

    private Connection connection;

    public WishlistDAOImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean addToWishlist(int userId, int productId) {

        boolean status = false;

        try {

            String query = "INSERT INTO wishlist(user_id, product_id) VALUES (?, ?)";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public boolean removeFromWishlist(int userId, int productId) {

        boolean status = false;

        try {

            String query = "DELETE FROM wishlist WHERE user_id=? AND product_id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public List<Product> getWishlistProducts(int userId) {

        List<Product> products = new ArrayList<>();

        try {

            String query = """
                    SELECT p.* FROM products p
                    JOIN wishlist w ON p.id = w.product_id
                    WHERE w.user_id = ?
                    """;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product product = new Product();

                product.setId(rs.getInt("id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));
                product.setFeatured(rs.getBoolean("is_featured"));
                product.setCreatedAt(rs.getTimestamp("created_at"));

                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public boolean isProductInWishlist(int userId, int productId) {

        boolean exists = false;

        try {

            String query = "SELECT * FROM wishlist WHERE user_id=? AND product_id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();

            exists = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }
}