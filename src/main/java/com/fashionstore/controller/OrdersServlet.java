package com.fashionstore.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;
import com.fashionstore.util.SessionAuth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAOImpl orderDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = SessionAuth.getUser(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=orders");
            return;
        }

        List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
        Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();

        for (Order order : orders) {
            orderItemsMap.put(
                    order.getId(),
                    orderDAO.getOrderItems(order.getId()));
        }

        request.setAttribute("orders", orders);
        request.setAttribute("orderItemsMap", orderItemsMap);

        request.getRequestDispatcher("/WEB-INF/views/orders.jsp")
                .forward(request, response);
    }
}
