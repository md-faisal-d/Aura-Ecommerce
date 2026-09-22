package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    public CategoryDAOImpl() {
        // Connection management per-method
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            if (connection == null) {
                return categories;
            }

            String query = "SELECT * FROM categories ORDER BY name";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] CategoryDAO.getAllCategories: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return categories;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Category category = null;
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            if (connection == null) {
                return null;
            }

            String query = "SELECT * FROM categories WHERE category_id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                category = mapCategory(rs);
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Error] CategoryDAO.getCategoryById: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return category;
    }
}
