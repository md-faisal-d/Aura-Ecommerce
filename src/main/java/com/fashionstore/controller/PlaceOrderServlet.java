package com.fashionstore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;
import com.fashionstore.util.SessionAuth;
import com.fashionstore.util.SessionCart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/place-order")
public class PlaceOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAOImpl orderDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAOImpl();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = SessionAuth.getUser(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<CartItem> cartItems = SessionCart.getItems(session);

        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(SessionCart.getTotal(session));
        order.setShippingAddress(request.getParameter("address"));
        order.setCity(request.getParameter("city"));
        order.setState(request.getParameter("state"));
        order.setPincode(request.getParameter("pincode"));
        order.setPaymentMethod(request.getParameter("paymentMethod"));
        order.setOrderStatus("Pending");

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setVariantId(cartItem.getVariantId());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getPrice());
            orderItems.add(item);
        }

        boolean placed = orderDAO.placeOrder(order, orderItems);

        if (placed) {
            new CartDAOImpl().clearCart(user.getId());
            SessionCart.clear(session);
            response.sendRedirect(
                    request.getContextPath() + "/order-success");
        } else {
            request.setAttribute("error", "Could not place order. Try again.");
            request.setAttribute("user", user);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", SessionCart.getTotal(session));
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")
                    .forward(request, response);
        }
    }
}
