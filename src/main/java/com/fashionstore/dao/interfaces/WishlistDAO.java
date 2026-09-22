// WishlistDAO.java

package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.Product;

public interface WishlistDAO {

    boolean addToWishlist(int userId, int productId);

    boolean removeFromWishlist(int userId, int productId);

    List<Product> getWishlistProducts(int userId);

    boolean isProductInWishlist(int userId, int productId);
}