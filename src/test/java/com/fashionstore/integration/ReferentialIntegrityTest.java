package com.fashionstore.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReferentialIntegrityTest {

    private static final String H2_URL = "jdbc:h2:mem:fk_testdb;MODE=MySQL;DB_CLOSE_DELAY=-1";

    @BeforeAll
    public static void setupSchema() throws Exception {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection(H2_URL, "sa", "")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE users (user_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100));");
            stmt.execute("CREATE TABLE cart (cart_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT, FOREIGN KEY (user_id) REFERENCES users(user_id));");
            stmt.execute("CREATE TABLE products (product_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100));");
            stmt.execute("CREATE TABLE product_variants (variant_id INT AUTO_INCREMENT PRIMARY KEY, product_id INT, size VARCHAR(10), color VARCHAR(10), FOREIGN KEY (product_id) REFERENCES products(product_id));");
            stmt.execute("CREATE TABLE cart_items (cart_item_id INT AUTO_INCREMENT PRIMARY KEY, cart_id INT, variant_id INT, quantity INT, FOREIGN KEY (cart_id) REFERENCES cart(cart_id), FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id));");

            stmt.execute("INSERT INTO users (user_id, name, email) VALUES (1, 'User 1', 'u1@test.com');");
            stmt.execute("INSERT INTO cart (cart_id, user_id) VALUES (1, 1);");
        }
    }

    @Test
    public void testInsertCartItemNonExistentVariantFailsFKConstraint() {
        assertThrows(SQLException.class, () -> {
            try (Connection conn = DriverManager.getConnection(H2_URL, "sa", "")) {
                String query = "INSERT INTO cart_items (cart_id, variant_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, 1);
                ps.setInt(2, 999999); // Non-existent variant ID
                ps.setInt(3, 1);
                ps.executeUpdate();
            }
        });
    }

    @Test
    public void testInsertCartItemNonExistentCartFailsFKConstraint() {
        assertThrows(SQLException.class, () -> {
            try (Connection conn = DriverManager.getConnection(H2_URL, "sa", "")) {
                String query = "INSERT INTO cart_items (cart_id, variant_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, 999999); // Non-existent cart ID
                ps.setInt(2, 1);
                ps.setInt(3, 1);
                ps.executeUpdate();
            }
        });
    }
}
