// CartDAO.java

package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.CartItem;

public interface CartDAO {

    boolean addToCart(CartItem cartItem);

    boolean updateCartItemQuantity(int cartItemId, int quantity);

    boolean removeCartItem(int cartItemId);

    List<CartItem> getCartItemsByUserId(int userId);

    double getCartTotal(int userId);

    boolean clearCart(int userId);
}