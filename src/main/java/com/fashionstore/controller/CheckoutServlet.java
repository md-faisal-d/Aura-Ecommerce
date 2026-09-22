package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;
import com.fashionstore.util.SessionAuth;
import com.fashionstore.util.SessionCart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = SessionAuth.getUser(session);
        List<CartItem> cartItems = SessionCart.getItems(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=checkout");
            return;
        }

        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", SessionCart.getTotal(session));

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")
                .forward(request, response);
    }
}
