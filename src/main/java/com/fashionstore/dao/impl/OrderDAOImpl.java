// OrderDAOImpl.java

package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    private Connection connection;

    public OrderDAOImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean placeOrder(Order order, List<OrderItem> orderItems) {

        boolean status = false;

        try {

            connection.setAutoCommit(false);

            String orderQuery = """
                    INSERT INTO orders
                    (user_id, total_amount, shipping_address,
                    city, state, pincode, payment_method, order_status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement orderPs =
                    connection.prepareStatement(
                            orderQuery,
                            Statement.RETURN_GENERATED_KEYS
                    );

            orderPs.setInt(1, order.getUserId());
            orderPs.setBigDecimal(2, order.getTotalAmount());
            orderPs.setString(3, order.getShippingAddress());
            orderPs.setString(4, order.getCity());
            orderPs.setString(5, order.getState());
            orderPs.setString(6, order.getPincode());
            orderPs.setString(7, order.getPaymentMethod());
            orderPs.setString(8, order.getOrderStatus());

            int orderInserted = orderPs.executeUpdate();

            if (orderInserted > 0) {

                ResultSet generatedKeys =
                        orderPs.getGeneratedKeys();

                if (generatedKeys.next()) {

                    int orderId = generatedKeys.getInt(1);

                    String itemQuery = """
                            INSERT INTO order_items
                            (order_id, variant_id, quantity, price)
                            VALUES (?, ?, ?, ?)
                            """;

                    PreparedStatement itemPs =
                            connection.prepareStatement(itemQuery);

                    for (OrderItem item : orderItems) {

                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, item.getVariantId());
                        itemPs.setInt(3, item.getQuantity());
                        itemPs.setBigDecimal(4, item.getPrice());

                        itemPs.addBatch();
                    }

                    itemPs.executeBatch();

                    connection.commit();

                    status = true;
                }
            }

        } catch (Exception e) {

            try {
                connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return status;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {

        List<Order> orders = new ArrayList<>();

        try {

            String query =
                    "SELECT * FROM orders WHERE user_id=? ORDER BY ordered_at DESC";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Order order = new Order();

                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setCity(rs.getString("city"));
                order.setState(rs.getString("state"));
                order.setPincode(rs.getString("pincode"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setOrderedAt(rs.getTimestamp("ordered_at"));

                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public Order getOrderById(int orderId) {

        Order order = null;

        try {

            String query = "SELECT * FROM orders WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                order = new Order();

                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setCity(rs.getString("city"));
                order.setState(rs.getString("state"));
                order.setPincode(rs.getString("pincode"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setOrderedAt(rs.getTimestamp("ordered_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        try {

            String query = """
                    SELECT oi.*, p.name, p.image_url, pv.size, pv.color
                    FROM order_items oi
                    JOIN product_variants pv ON oi.variant_id = pv.id
                    JOIN products p ON pv.product_id = p.id
                    WHERE oi.order_id=?
                    """;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderItem item = new OrderItem();

                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setVariantId(rs.getInt("variant_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setProductName(rs.getString("name"));
                item.setImageUrl(rs.getString("image_url"));
                item.setSize(rs.getString("size"));
                item.setColor(rs.getString("color"));

                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String statusText) {

        boolean status = false;

        try {

            String query =
                    "UPDATE orders SET order_status=? WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, statusText);
            ps.setInt(2, orderId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}