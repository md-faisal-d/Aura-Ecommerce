package com.fashionstore.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.http.HttpSession;

public final class SessionCart {

    public static final String CART_ATTR = "cart";

    private SessionCart() {
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, CartItem> getCartMap(HttpSession session) {
        Map<Integer, CartItem> cart =
                (Map<Integer, CartItem>) session.getAttribute(CART_ATTR);

        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_ATTR, cart);
        }

        return cart;
    }

    public static List<CartItem> getItems(HttpSession session) {
        return new ArrayList<>(getCartMap(session).values());
    }

    public static int getItemCount(HttpSession session) {
        int count = 0;
        for (CartItem item : getCartMap(session).values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public static BigDecimal getTotal(HttpSession session) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : getCartMap(session).values()) {
            total = total.add(
                    item.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    public static boolean addProduct(
            HttpSession session,
            int productId,
            int variantId,
            int quantity) {

        ProductDAOImpl productDAO = new ProductDAOImpl();
        Product product = productDAO.getProductById(productId);

        if (product == null) {
            return false;
        }

        List<ProductVariant> variants =
                productDAO.getVariantsByProductId(productId);

        ProductVariant variant = null;

        if (variantId > 0) {
            for (ProductVariant v : variants) {
                if (v.getId() == variantId) {
                    variant = v;
                    break;
                }
            }
        } else if (!variants.isEmpty()) {
            variant = variants.get(0);
            variantId = variant.getId();
        }

        Map<Integer, CartItem> cart = getCartMap(session);
        int cartKey = (variantId > 0) ? variantId : productId;
        CartItem existing = cart.get(cartKey);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setVariantId(variantId > 0 ? variantId : productId);
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setProductName(product.getName());
            item.setImageUrl(product.getImageUrl());
            item.setPrice(product.getPrice());
            item.setSize(variant != null ? variant.getSize() : "M");
            item.setColor(variant != null ? variant.getColor() : "Standard");
            cart.put(cartKey, item);
        }

        return true;
    }

    public static boolean updateQuantity(
            HttpSession session,
            int variantId,
            int quantity) {

        Map<Integer, CartItem> cart = getCartMap(session);
        CartItem item = cart.get(variantId);

        if (item == null) {
            return false;
        }

        if (quantity <= 0) {
            cart.remove(variantId);
        } else {
            item.setQuantity(quantity);
        }

        return true;
    }

    public static boolean removeItem(HttpSession session, int variantId) {
        return getCartMap(session).remove(variantId) != null;
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(CART_ATTR);
    }
}
