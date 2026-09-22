package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class UserDAOTest {

    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testRegisterUserSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = new User();
            user.setName("John Doe");
            user.setEmail("john_" + System.currentTimeMillis() + "@example.com");
            user.setPassword("secret123");
            user.setPhone("1234567890");

            boolean result = userDAO.registerUser(user);
            assertTrue(result);
            assertTrue(user.getId() > 0);
        }
    }

    @Test
    public void testRegisterUserFailureDuplicateEmail() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            String email = "dup_" + System.currentTimeMillis() + "@example.com";
            User user1 = new User();
            user1.setName("User 1");
            user1.setEmail(email);
            user1.setPassword("pass");
            userDAO.registerUser(user1);

            User user2 = new User();
            user2.setName("User 2");
            user2.setEmail(email);
            user2.setPassword("pass");

            boolean result = userDAO.registerUser(user2);
            assertFalse(result, "Duplicate email registration should return false");
        }
    }

    @Test
    public void testLoginUserSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            String email = "login_" + System.currentTimeMillis() + "@example.com";
            User user = new User();
            user.setName("Login User");
            user.setEmail(email);
            user.setPassword("correctpass");
            userDAO.registerUser(user);

            User loggedIn = userDAO.loginUser(email, "correctpass");
            assertNotNull(loggedIn);
            assertEquals("Login User", loggedIn.getName());
        }
    }

    @Test
    public void testLoginUserFailureInvalidPassword() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            String email = "login_fail_" + System.currentTimeMillis() + "@example.com";
            User user = new User();
            user.setName("Login User");
            user.setEmail(email);
            user.setPassword("correctpass");
            userDAO.registerUser(user);

            User loggedIn = userDAO.loginUser(email, "wrongpass");
            assertNull(loggedIn);
        }
    }

    @Test
    public void testGetUserByIdNotFound() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = userDAO.getUserById(99999);
            assertNull(user);
        }
    }

    @Test
    public void testDeleteUserSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            String email = "del_" + System.currentTimeMillis() + "@example.com";
            User user = new User();
            user.setName("Delete User");
            user.setEmail(email);
            user.setPassword("pass");
            userDAO.registerUser(user);

            boolean status = userDAO.deleteUser(user.getId());
            assertTrue(status);

            User fetched = userDAO.getUserById(user.getId());
            assertNull(fetched);
        }
    }
}
