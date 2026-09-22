package com.fashionstore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.util.SessionWishlist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAOImpl productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Set<Integer> productIds = SessionWishlist.getProductIds(session);
        List<Product> products = new ArrayList<>();

        for (int productId : productIds) {
            Product product = productDAO.getProductById(productId);
            if (product != null) {
                products.add(product);
            }
        }

        request.setAttribute("products", products);

        request.getRequestDispatcher("/WEB-INF/views/wishlist.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        int productId = Integer.parseInt(request.getParameter("productId"));
        String action = request.getParameter("action");

        if ("remove".equals(action)) {
            SessionWishlist.remove(session, productId);
        } else {
            SessionWishlist.add(session, productId);
        }

        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank()) {
            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }
}
