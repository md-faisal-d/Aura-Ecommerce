package com.fashionstore.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.model.CartItem;
import com.fashionstore.util.SessionCart;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart-api")
public class CartApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        int variantId = Integer.parseInt(request.getParameter("variantId"));

        if ("increment".equals(action)) {
            CartItem item = SessionCart.getCartMap(session).get(variantId);
            if (item != null) {
                SessionCart.updateQuantity(
                        session, variantId, item.getQuantity() + 1);
            }
        } else if ("decrement".equals(action)) {
            CartItem item = SessionCart.getCartMap(session).get(variantId);
            if (item != null) {
                SessionCart.updateQuantity(
                        session, variantId, item.getQuantity() - 1);
            }
        } else if ("remove".equals(action)) {
            SessionCart.removeItem(session, variantId);
        } else if ("set".equals(action)) {
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            SessionCart.updateQuantity(session, variantId, quantity);
        }

        writeJson(session, response);
    }

    private void writeJson(HttpSession session, HttpServletResponse response)
            throws IOException {

        List<CartItem> items = SessionCart.getItems(session);
        BigDecimal total = SessionCart.getTotal(session);
        int count = SessionCart.getItemCount(session);
        int lineCount = items.size();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("{\"count\":").append(count);
        json.append(",\"lineCount\":").append(lineCount);
        json.append(",\"total\":").append(total);
        json.append(",\"items\":[");

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{");
            json.append("\"variantId\":").append(item.getVariantId());
            json.append(",\"quantity\":").append(item.getQuantity());
            json.append(",\"price\":").append(item.getPrice());
            json.append(",\"subtotal\":").append(item.getSubtotal());
            json.append("}");
        }

        json.append("]}");
        out.print(json);
        out.flush();
    }
}
