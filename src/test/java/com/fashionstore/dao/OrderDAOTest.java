package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class OrderDAOTest {

    private OrderDAOImpl orderDAO;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        orderDAO = new OrderDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testPlaceOrderAndGetOrdersByUserId() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = new User();
            user.setName("Order User");
            user.setEmail("order_" + System.currentTimeMillis() + "@example.com");
            user.setPassword("pass");
            userDAO.registerUser(user);

            Order order = new Order();
            order.setUserId(user.getId());
            order.setShippingAddress("123 Main St");
            order.setCity("City");
            order.setState("State");
            order.setPincode("12345");
            order.setTotalAmount(new BigDecimal("1500.00"));
            order.setOrderStatus("PLACED");

            List<OrderItem> items = new ArrayList<>();
            OrderItem item = new OrderItem();
            item.setVariantId(1);
            item.setQuantity(1);
            item.setPrice(new BigDecimal("1500.00"));
            items.add(item);

            boolean placed = orderDAO.placeOrder(order, items);
            assertTrue(placed);
            assertTrue(order.getId() > 0);

            List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
            assertNotNull(orders);
            assertEquals(1, orders.size());
            assertEquals("PLACED", orders.get(0).getOrderStatus());
        }
    }

    @Test
    public void testGetOrderByIdNotFound() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            Order order = orderDAO.getOrderById(99999);
            assertNull(order);
        }
    }

    @Test
    public void testUpdateOrderStatusFailureInvalidId() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            boolean status = orderDAO.updateOrderStatus(99999, "SHIPPED");
            assertFalse(status);
        }
    }
}
