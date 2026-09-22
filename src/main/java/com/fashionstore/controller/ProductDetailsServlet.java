package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ReviewDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.model.Review;
import com.fashionstore.util.SessionWishlist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAOImpl productDAO;
    private ReviewDAOImpl reviewDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        reviewDAO = new ReviewDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int productId = 0;
        try {
            productId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        List<ProductVariant> variants =
                productDAO.getVariantsByProductId(productId);
        List<Review> reviews = reviewDAO.getReviewsByProductId(productId);
        double averageRating = reviewDAO.getAverageRating(productId);

        request.setAttribute("product", product);
        request.setAttribute("variants", variants);
        request.setAttribute("reviews", reviews);
        request.setAttribute("averageRating", averageRating);
        request.setAttribute(
                "inWishlist",
                SessionWishlist.contains(request.getSession(), productId));

        request.getRequestDispatcher("/WEB-INF/views/product-details.jsp")
                .forward(request, response);
    }
}
