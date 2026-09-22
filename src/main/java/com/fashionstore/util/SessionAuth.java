package com.fashionstore.util;

import com.fashionstore.model.User;

import jakarta.servlet.http.HttpSession;

public final class SessionAuth {

    public static final String USER_ATTR = "user";

    private SessionAuth() {
    }

    public static User getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(USER_ATTR);
        if (value instanceof User user) {
            return user;
        }
        return null;
    }

    public static void login(HttpSession session, User user) {
        session.setAttribute(USER_ATTR, user);
    }

    public static void logout(HttpSession session) {
        session.removeAttribute(USER_ATTR);
    }
}
