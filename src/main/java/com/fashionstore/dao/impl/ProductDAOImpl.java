package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    public ProductDAOImpl() {
        // Connection management handled per-method
    }

    private Connection openConnection() {
        return DBConnection.getConnection();
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        try {
            product.setId(rs.getInt("product_id"));
        } catch (SQLException e) {
            product.setId(rs.getInt("id"));
        }

        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setBrand(rs.getString("brand"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImageUrl(rs.getString("image_url"));
        product.setFeatured(rs.getBoolean("is_featured"));
        product.setCreatedAt(rs.getTimestamp("created_at"));

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            String query = "SELECT * FROM products";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public Product getProductById(int productId) {
        Product product = null;
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return null;

            String query = "SELECT * FROM products WHERE product_id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = mapProduct(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return product;
    }

    @Override
    public List<Product> getFeaturedProducts() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            String query = "SELECT * FROM products WHERE is_featured = 1 OR is_featured = true";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public List<Product> getNewArrivals() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            String query = "SELECT * FROM products ORDER BY created_at DESC LIMIT 10";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            String query = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ? OR brand LIKE ?";
            PreparedStatement ps = connection.prepareStatement(query);
            String searchKeyword = "%" + keyword + "%";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            String query = "SELECT * FROM products WHERE category_id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public List<Product> filterProducts(
            int categoryId,
            String size,
            String color,
            double minPrice,
            double maxPrice,
            String brand,
            String sort) {

        List<Product> products = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return products;

            StringBuilder query = new StringBuilder("SELECT DISTINCT p.* FROM products p ");
            boolean needsVariantJoin = (size != null && !size.isEmpty()) || (color != null && !color.isEmpty());

            if (needsVariantJoin) {
                query.append("INNER JOIN product_variants pv ON p.product_id = pv.product_id ");
            }

            query.append("WHERE p.price BETWEEN ? AND ? ");

            if (categoryId > 0) {
                query.append("AND p.category_id = ? ");
            }

            if (brand != null && !brand.isEmpty()) {
                query.append("AND p.brand = ? ");
            }

            if (size != null && !size.isEmpty()) {
                query.append("AND pv.size = ? ");
            }

            if (color != null && !color.isEmpty()) {
                query.append("AND pv.color = ? ");
            }

            if (sort != null && !sort.isEmpty()) {
                if (sort.equals("priceLowToHigh")) {
                    query.append("ORDER BY p.price ASC ");
                } else if (sort.equals("priceHighToLow")) {
                    query.append("ORDER BY p.price DESC ");
                }
            }

            PreparedStatement ps = connection.prepareStatement(query.toString());
            int index = 1;

            ps.setDouble(index++, minPrice);
            ps.setDouble(index++, maxPrice);

            if (categoryId > 0) {
                ps.setInt(index++, categoryId);
            }

            if (brand != null && !brand.isEmpty()) {
                ps.setString(index++, brand);
            }

            if (size != null && !size.isEmpty()) {
                ps.setString(index++, size);
            }

            if (color != null && !color.isEmpty()) {
                ps.setString(index++, color);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return products;
    }

    @Override
    public List<ProductVariant> getVariantsByProductId(int productId) {
        List<ProductVariant> variants = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return variants;

            String query = "SELECT * FROM product_variants WHERE product_id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductVariant variant = new ProductVariant();

                try {
                    variant.setId(rs.getInt("variant_id"));
                } catch (SQLException e) {
                    variant.setId(rs.getInt("id"));
                }
                variant.setProductId(rs.getInt("product_id"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStock(rs.getInt("stock"));

                variants.add(variant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return variants;
    }

    @Override
    public boolean addProduct(Product product) {
        return false;
    }

    @Override
    public boolean updateProduct(Product product) {
        return false;
    }

    @Override
    public boolean deleteProduct(int productId) {
        return false;
    }

    @Override
    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        Connection connection = null;

        try {
            connection = openConnection();
            if (connection == null) return brands;

            String query = "SELECT DISTINCT brand FROM products WHERE brand IS NOT NULL AND brand != '' ORDER BY brand";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(connection);
        }

        return brands;
    }
}