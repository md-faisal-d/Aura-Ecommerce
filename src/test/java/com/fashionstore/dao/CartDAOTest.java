package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class CartDAOTest {

    private CartDAOImpl cartDAO;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        cartDAO = new CartDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testAddToCartAndGetCartItems() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = new User();
            user.setName("Cart User");
            user.setEmail("cart_" + System.currentTimeMillis() + "@example.com");
            user.setPassword("pass");
            userDAO.registerUser(user);

            boolean status = cartDAO.addToCart(user.getId(), 1, 3);
            assertTrue(status);

            List<CartItem> items = cartDAO.getCartItemsByUserId(user.getId());
            assertNotNull(items);
            assertEquals(1, items.size());
            assertEquals(3, items.get(0).getQuantity());

            double total = cartDAO.getCartTotal(user.getId());
            assertEquals(4500.00, total, 0.01);
        }
    }

    @Test
    public void testRemoveCartItemFailureNonExistentId() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            boolean status = cartDAO.removeCartItem(99999);
            assertFalse(status);
        }
    }

    @Test
    public void testGetCartTotalEmptyCart() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            double total = cartDAO.getCartTotal(99999);
            assertEquals(0.0, total, 0.001);
        }
    }
}
