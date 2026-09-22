// CartDAOImpl.java

package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.interfaces.CartDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

public class CartDAOImpl implements CartDAO {

    private Connection connection;

    public CartDAOImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean addToCart(CartItem cartItem) {

        boolean status = false;

        try {

            String query = """
                    INSERT INTO cart_items(cart_id, variant_id, quantity)
                    VALUES (?, ?, ?)
                    """;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getVariantId());
            ps.setInt(3, cartItem.getQuantity());

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {

        boolean status = false;

        try {

            String query =
                    "UPDATE cart_items SET quantity=? WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public boolean removeCartItem(int cartItemId) {

        boolean status = false;

        try {

            String query = "DELETE FROM cart_items WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, cartItemId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {

        List<CartItem> items = new ArrayList<>();

        try {

            String query =
                    "SELECT ci.*, " +
                    "p.name, " +
                    "p.image_url, " +
                    "p.price, " +
                    "pv.size, " +
                    "pv.color " +
                    "FROM cart c " +
                    "JOIN cart_items ci " +
                    "ON c.id = ci.cart_id " +
                    "JOIN product_variants pv " +
                    "ON ci.variant_id = pv.id " +
                    "JOIN products p " +
                    "ON pv.product_id = p.id " +
                    "WHERE c.user_id=?";

            PreparedStatement ps =
                    connection.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                CartItem item = new CartItem();

                item.setId(rs.getInt("id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setVariantId(rs.getInt("variant_id"));
                item.setQuantity(rs.getInt("quantity"));

                item.setProductName(
                        rs.getString("name"));

                item.setImageUrl(
                        rs.getString("image_url"));

                item.setPrice(
                        rs.getBigDecimal("price"));

                item.setSize(
                        rs.getString("size"));

                item.setColor(
                        rs.getString("color"));

                items.add(item);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return items;
    }

    @Override
    public double getCartTotal(int userId) {

        double total = 0;

        try {

            String query = """
                    SELECT SUM(p.price * ci.quantity) AS total
                    FROM cart_items ci
                    JOIN cart c ON ci.cart_id = c.id
                    JOIN product_variants pv ON ci.variant_id = pv.id
                    JOIN products p ON pv.product_id = p.id
                    WHERE c.user_id = ?
                    """;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public boolean clearCart(int userId) {

        boolean status = false;

        try {

            String query = """
                    DELETE ci
                    FROM cart_items ci
                    JOIN cart c
                    ON ci.cart_id = c.id
                    WHERE c.user_id = ?
                    """;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, userId);

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
    public boolean addToCart(
            int userId,
            int variantId,
            int quantity) {

        try {

            int cartId = getCartIdByUser(userId);

            if(cartId == 0) {

                String createCart =
                        "INSERT INTO cart(user_id) VALUES(?)";

                PreparedStatement cps =
                        connection.prepareStatement(
                                createCart,
                                PreparedStatement.RETURN_GENERATED_KEYS);

                cps.setInt(1, userId);

                cps.executeUpdate();

                ResultSet generatedKeys =
                        cps.getGeneratedKeys();

                if(generatedKeys.next()) {

                    cartId = generatedKeys.getInt(1);
                }
            }

            String checkQuery =
                    "SELECT * FROM cart_items " +
                    "WHERE cart_id=? AND variant_id=?";

            PreparedStatement checkPs =
                    connection.prepareStatement(checkQuery);

            checkPs.setInt(1, cartId);
            checkPs.setInt(2, variantId);

            ResultSet rs = checkPs.executeQuery();

            if(rs.next()) {

                int existingQty =
                        rs.getInt("quantity");

                String updateQuery =
                        "UPDATE cart_items " +
                        "SET quantity=? " +
                        "WHERE cart_id=? AND variant_id=?";

                PreparedStatement ups =
                        connection.prepareStatement(updateQuery);

                ups.setInt(1,
                        existingQty + quantity);

                ups.setInt(2, cartId);
                ups.setInt(3, variantId);

                ups.executeUpdate();

            } else {

                String insertQuery =
                        "INSERT INTO cart_items" +
                        "(cart_id, variant_id, quantity) " +
                        "VALUES(?,?,?)";

                PreparedStatement ips =
                        connection.prepareStatement(insertQuery);

                ips.setInt(1, cartId);
                ips.setInt(2, variantId);
                ips.setInt(3, quantity);

                ips.executeUpdate();
            }

            return true;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public int getCartIdByUser(int userId) {

        try {

            String query =
                    "SELECT id FROM cart WHERE user_id=?";

            PreparedStatement ps =
                    connection.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return rs.getInt("id");
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public boolean updateQuantity(
            int cartItemId,
            int quantity) {

        try {

            String query =
                    "UPDATE cart_items " +
                    "SET quantity=? " +
                    "WHERE id=?";

            PreparedStatement ps =
                    connection.prepareStatement(query);

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}