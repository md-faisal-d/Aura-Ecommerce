package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.model.Category;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories =
                new CategoryDAOImpl().getAllCategories();

        request.setAttribute("categories", categories);

        // Forward to JSP
        RequestDispatcher dispatcher =
                request.getRequestDispatcher(
                        "/WEB-INF/views/home.jsp");

        dispatcher.forward(request, response);
    }
}