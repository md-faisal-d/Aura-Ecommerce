package com.fashionstore.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

public class FullE2EFlowIntegrationTest {

    private static final String H2_URL = "jdbc:h2:mem:e2e_testdb;MODE=MySQL;DB_CLOSE_DELAY=-1";

    @BeforeAll
    public static void setupTestDatabase() throws Exception {
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

            stmt.execute("INSERT INTO categories (category_id, name) VALUES (1, 'Men');");

            stmt.execute("CREATE TABLE products (" +
                    "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(150) NOT NULL, " +
                    "description TEXT, " +
                    "price DECIMAL(10,2) NOT NULL, " +
                    "category_id INT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "image_url VARCHAR(500), " +
                    "FOREIGN KEY (category_id) REFERENCES categories(category_id));");

            stmt.execute("INSERT INTO products (product_id, name, price, category_id) VALUES (1, 'Test Shirt', 1500.00, 1);");

            stmt.execute("CREATE TABLE product_variants (" +
                    "variant_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "product_id INT NOT NULL, " +
                    "size VARCHAR(10) NOT NULL, " +
                    "color VARCHAR(50) NOT NULL, " +
                    "stock INT DEFAULT 100, " +
                    "FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE);");

            stmt.execute("INSERT INTO product_variants (variant_id, product_id, size, color, stock) VALUES (10, 1, 'M', 'Black', 50);");

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
        }
    }

    @Test
    public void testFullE2EFlow() throws Exception {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> DriverManager.getConnection(H2_URL, "sa", ""));

            UserDAOImpl userDAO = new UserDAOImpl();
            CartDAOImpl cartDAO = new CartDAOImpl();
            OrderDAOImpl orderDAO = new OrderDAOImpl();

            // 1. REGISTER
            User user = new User();
            user.setName("E2E User");
            user.setEmail("e2e@example.com");
            user.setPassword("password123");
            user.setPhone("9998887770");
            user.setAddress("123 Test Street");
            user.setCity("TestCity");
            user.setState("TestState");
            user.setPincode("123456");

            boolean regResult = userDAO.registerUser(user);
            assertTrue(regResult, "Registration should succeed");
            assertTrue(user.getId() > 0, "Registered user ID should be generated");

            // 2. LOGIN
            User loggedIn = userDAO.loginUser("e2e@example.com", "password123");
            assertNotNull(loggedIn, "Login should return authenticated user");
            assertEquals(user.getId(), loggedIn.getId());

            // 3. ADD TO CART
            boolean cartResult = cartDAO.addToCart(loggedIn.getId(), 10, 2);
            assertTrue(cartResult, "Item should be added to cart");

            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(loggedIn.getId());
            assertEquals(1, cartItems.size(), "Cart should contain 1 item");
            assertEquals(2, cartItems.get(0).getQuantity());

            double cartTotal = cartDAO.getCartTotal(loggedIn.getId());
            assertEquals(3000.00, cartTotal, 0.01, "Cart total should be 3000.00");

            // 4. PLACE ORDER
            Order order = new Order();
            order.setUserId(loggedIn.getId());
            order.setShippingAddress("123 Test Street");
            order.setCity("TestCity");
            order.setState("TestState");
            order.setPincode("123456");
            order.setTotalAmount(new BigDecimal("3000.00"));
            order.setOrderStatus("PLACED");

            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem item = new OrderItem();
            item.setVariantId(10);
            item.setQuantity(2);
            item.setPrice(new BigDecimal("1500.00"));
            orderItems.add(item);

            boolean orderResult = orderDAO.placeOrder(order, orderItems);
            assertTrue(orderResult, "Order placement should succeed");
            assertTrue(order.getId() > 0, "Order ID should be generated");

            // 5. VIEW ORDER HISTORY
            List<Order> orderHistory = orderDAO.getOrdersByUserId(loggedIn.getId());
            assertEquals(1, orderHistory.size(), "User should have 1 order in history");
            Order fetchedOrder = orderHistory.get(0);
            assertEquals(new BigDecimal("3000.00"), fetchedOrder.getTotalAmount());
            assertEquals("PLACED", fetchedOrder.getOrderStatus());
        }
    }
}
