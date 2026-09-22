package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.impl.ReviewDAOImpl;
import com.fashionstore.model.Review;
import com.fashionstore.model.User;
import com.fashionstore.util.SessionAuth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ReviewDAOImpl reviewDAO;

    @Override
    public void init() {
        reviewDAO = new ReviewDAOImpl();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        User user = SessionAuth.getUser(session);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Review review = new Review();
        review.setUserId(user.getId());
        review.setProductId(Integer.parseInt(request.getParameter("productId")));
        review.setRating(Integer.parseInt(request.getParameter("rating")));
        review.setReviewText(request.getParameter("reviewText"));

        reviewDAO.addReview(review);

        response.sendRedirect(
                request.getContextPath()
                        + "/product?id="
                        + review.getProductId());
    }
}
