// OrderDAO.java

package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;

public interface OrderDAO {

    boolean placeOrder(Order order, List<OrderItem> orderItems);

    List<Order> getOrdersByUserId(int userId);

    Order getOrderById(int orderId);

    List<OrderItem> getOrderItems(int orderId);

    boolean updateOrderStatus(int orderId, String status);
}