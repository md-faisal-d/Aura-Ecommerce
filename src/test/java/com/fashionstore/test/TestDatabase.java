package com.fashionstore.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestDatabase {

    private static final String H2_URL = "jdbc:h2:mem:fashion_store_unit_test;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;
        try {
            Class.forName("org.h2.Driver");
            try (Connection conn = DriverManager.getConnection(H2_URL, "sa", "")) {
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE users (" +
                        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(150) UNIQUE NOT NULL, " +
                        "password_hash VARCHAR(255) NOT NULL, " +
                        "phone VARCHAR(20), " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

                stmt.execute("CREATE TABLE addresses (" +
                        "address_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "full_name VARCHAR(100) NOT NULL, " +
                        "phone VARCHAR(20) NOT NULL, " +
                        "address_line1 VARCHAR(255) NOT NULL, " +
                        "address_line2 VARCHAR(255), " +
                        "city VARCHAR(100) NOT NULL, " +
                        "state VARCHAR(100) NOT NULL, " +
                        "postal_code VARCHAR(20) NOT NULL, " +
                        "country VARCHAR(100) NOT NULL, " +
                        "is_default TINYINT DEFAULT 0, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE);");

                stmt.execute("CREATE TABLE categories (" +
                        "category_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(100) UNIQUE NOT NULL);");
                stmt.execute("INSERT INTO categories (category_id, name) VALUES (1, 'Men'), (2, 'Women');");

                stmt.execute("CREATE TABLE products (" +
                        "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(150) NOT NULL, " +
                        "brand VARCHAR(100) DEFAULT 'Aura', " +
                        "description TEXT, " +
                        "price DECIMAL(10,2) NOT NULL, " +
                        "category_id INT NOT NULL, " +
                        "is_featured TINYINT DEFAULT 1, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "image_url VARCHAR(500), " +
                        "FOREIGN KEY (category_id) REFERENCES categories(category_id));");
                stmt.execute("INSERT INTO products (product_id, name, brand, description, price, category_id, is_featured) VALUES (1, 'Test Shirt', 'Aura', 'A nice tee', 1500.00, 1, 1);");

                stmt.execute("CREATE TABLE product_variants (" +
                        "variant_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "product_id INT NOT NULL, " +
                        "size VARCHAR(10) NOT NULL, " +
                        "color VARCHAR(50) NOT NULL, " +
                        "stock INT DEFAULT 10, " +
                        "FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE);");
                stmt.execute("INSERT INTO product_variants (variant_id, product_id, size, color, stock) VALUES (1, 1, 'M', 'Black', 10);");

                stmt.execute("CREATE TABLE cart (" +
                        "cart_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT UNIQUE NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE);");

                stmt.execute("CREATE TABLE cart_items (" +
                        "cart_item_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "cart_id INT NOT NULL, " +
                        "variant_id INT NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id));");

                stmt.execute("CREATE TABLE orders (" +
                        "order_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "address_id INT NOT NULL, " +
                        "total_amount DECIMAL(10,2) NOT NULL, " +
                        "status VARCHAR(50) DEFAULT 'PLACED', " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (address_id) REFERENCES addresses(address_id));");

                stmt.execute("CREATE TABLE order_items (" +
                        "order_item_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "order_id INT NOT NULL, " +
                        "variant_id INT NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "price DECIMAL(10,2) NOT NULL, " +
                        "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id));");

                stmt.execute("CREATE TABLE reviews (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "product_id INT NOT NULL, " +
                        "rating INT NOT NULL, " +
                        "review_text TEXT NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (product_id) REFERENCES products(product_id));");

                stmt.execute("CREATE TABLE wishlist (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "product_id INT NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (product_id) REFERENCES products(product_id));");
            }
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize H2 test database", e);
        }
    }

    public static Connection getConnection() {
        init();
        try {
            return DriverManager.getConnection(H2_URL, "sa", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
