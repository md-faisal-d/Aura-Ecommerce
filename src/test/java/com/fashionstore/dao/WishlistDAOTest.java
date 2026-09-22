package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.dao.impl.WishlistDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.User;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class WishlistDAOTest {

    private WishlistDAOImpl wishlistDAO;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        wishlistDAO = new WishlistDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testAddToWishlistAndCheck() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = new User();
            user.setName("Wishlist User");
            user.setEmail("wish_" + System.currentTimeMillis() + "@example.com");
            user.setPassword("pass");
            userDAO.registerUser(user);

            boolean added = wishlistDAO.addToWishlist(user.getId(), 1);
            assertTrue(added);

            boolean exists = wishlistDAO.isProductInWishlist(user.getId(), 1);
            assertTrue(exists);

            List<Product> products = wishlistDAO.getWishlistProducts(user.getId());
            assertEquals(1, products.size());

            boolean removed = wishlistDAO.removeFromWishlist(user.getId(), 1);
            assertTrue(removed);

            assertFalse(wishlistDAO.isProductInWishlist(user.getId(), 1));
        }
    }

    @Test
    public void testIsProductInWishlistFalse() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            boolean inWishlist = wishlistDAO.isProductInWishlist(99999, 99999);
            assertFalse(inWishlist);
        }
    }
}
