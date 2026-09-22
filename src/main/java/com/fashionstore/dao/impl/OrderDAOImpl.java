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

    public OrderDAOImpl() {
        // Connection management handled per-method
    }

    private Connection openConnection() {
        return DBConnection.getConnection();
    }

    @Override
    public boolean placeOrder(Order order, List<OrderItem> orderItems) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false);

            int addressId = order.getAddressId();
            if (addressId == 0) {
                String addrQuery = "INSERT INTO addresses (user_id, full_name, phone, address_line1, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement addrPs = conn.prepareStatement(addrQuery, Statement.RETURN_GENERATED_KEYS);
                addrPs.setInt(1, order.getUserId());
                addrPs.setString(2, "Customer");
                addrPs.setString(3, "");
                addrPs.setString(4, order.getShippingAddress() != null ? order.getShippingAddress() : "Default Address");
                addrPs.setString(5, order.getCity() != null ? order.getCity() : "");
                addrPs.setString(6, order.getState() != null ? order.getState() : "");
                addrPs.setString(7, order.getPincode() != null ? order.getPincode() : "");
                addrPs.setString(8, "India");

                addrPs.executeUpdate();
                ResultSet addrKeys = addrPs.getGeneratedKeys();
                if (addrKeys.next()) {
                    addressId = addrKeys.getInt(1);
                    order.setAddressId(addressId);
                }
            }

            String orderQuery = "INSERT INTO orders (user_id, address_id, total_amount, status) VALUES (?, ?, ?, ?)";
            PreparedStatement orderPs = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, order.getUserId());
            orderPs.setInt(2, addressId);
            orderPs.setBigDecimal(3, order.getTotalAmount());
            orderPs.setString(4, order.getOrderStatus() != null ? order.getOrderStatus() : "PLACED");

            int orderInserted = orderPs.executeUpdate();

            if (orderInserted > 0) {
                ResultSet generatedKeys = orderPs.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    order.setId(orderId);

                    String itemQuery = "INSERT INTO order_items (order_id, variant_id, quantity, price) VALUES (?, ?, ?, ?)";
                    PreparedStatement itemPs = conn.prepareStatement(itemQuery);

                    for (OrderItem item : orderItems) {
                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, item.getVariantId());
                        itemPs.setInt(3, item.getQuantity());
                        itemPs.setBigDecimal(4, item.getPrice());
                        itemPs.addBatch();
                    }

                    itemPs.executeBatch();
                    conn.commit();
                    status = true;
                }
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                DBConnection.closeQuietly(conn);
            }
        }

        return status;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return orders;

            String query =
                    "SELECT o.order_id, o.user_id, o.address_id, o.total_amount, o.status, o.created_at, " +
                    "a.address_line1, a.city, a.state, a.postal_code " +
                    "FROM orders o " +
                    "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                    "WHERE o.user_id=? ORDER BY o.created_at DESC";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();

                order.setId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setAddressId(rs.getInt("address_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setShippingAddress(rs.getString("address_line1"));
                order.setCity(rs.getString("city"));
                order.setState(rs.getString("state"));
                order.setPincode(rs.getString("postal_code"));
                order.setOrderStatus(rs.getString("status"));
                order.setOrderedAt(rs.getTimestamp("created_at"));

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return orders;
    }

    @Override
    public Order getOrderById(int orderId) {
        Order order = null;
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return null;

            String query =
                    "SELECT o.order_id, o.user_id, o.address_id, o.total_amount, o.status, o.created_at, " +
                    "a.address_line1, a.city, a.state, a.postal_code " +
                    "FROM orders o " +
                    "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                    "WHERE o.order_id=?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new Order();

                order.setId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setAddressId(rs.getInt("address_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setShippingAddress(rs.getString("address_line1"));
                order.setCity(rs.getString("city"));
                order.setState(rs.getString("state"));
                order.setPincode(rs.getString("postal_code"));
                order.setOrderStatus(rs.getString("status"));
                order.setOrderedAt(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return order;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return items;

            String query =
                    "SELECT oi.order_item_id, oi.order_id, oi.variant_id, oi.quantity, oi.price, " +
                    "p.name, p.image_url, pv.size, pv.color " +
                    "FROM order_items oi " +
                    "JOIN product_variants pv ON oi.variant_id = pv.variant_id " +
                    "JOIN products p ON pv.product_id = p.product_id " +
                    "WHERE oi.order_id=?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();

                item.setId(rs.getInt("order_item_id"));
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
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return items;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String statusText) {
        boolean status = false;
        Connection conn = null;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "UPDATE orders SET status=? WHERE order_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, statusText);
            ps.setInt(2, orderId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }
}