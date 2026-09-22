package com.fashionstore.util;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

public final class SessionWishlist {

    public static final String WISHLIST_ATTR = "wishlist";

    private SessionWishlist() {
    }

    @SuppressWarnings("unchecked")
    public static Set<Integer> getProductIds(HttpSession session) {
        Set<Integer> wishlist =
                (Set<Integer>) session.getAttribute(WISHLIST_ATTR);

        if (wishlist == null) {
            wishlist = new LinkedHashSet<>();
            session.setAttribute(WISHLIST_ATTR, wishlist);
        }

        return wishlist;
    }

    public static boolean add(HttpSession session, int productId) {
        return getProductIds(session).add(productId);
    }

    public static boolean remove(HttpSession session, int productId) {
        return getProductIds(session).remove(productId);
    }

    public static boolean contains(HttpSession session, int productId) {
        return getProductIds(session).contains(productId);
    }
}
