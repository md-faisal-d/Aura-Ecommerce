package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAOImpl productDAO;
    private CategoryDAOImpl categoryDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<Product> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productDAO.searchProducts(keyword.trim());
            request.setAttribute("keyword", keyword);
        } else {
            int categoryId = parseInt(request.getParameter("categoryId"), 0);
            String brand = emptyToNull(request.getParameter("brand"));
            String size = emptyToNull(request.getParameter("size"));
            String color = emptyToNull(request.getParameter("color"));
            String sort = emptyToNull(request.getParameter("sort"));

            boolean filterFormSubmitted =
                    "1".equals(request.getParameter("filter"));

            double minPrice = 0;
            double maxPrice = 999999;

            if (filterFormSubmitted) {
                minPrice = parseDouble(request.getParameter("minPrice"), 0);
                maxPrice = parseDouble(request.getParameter("maxPrice"), 999999);
            }

            boolean useFilter = categoryId > 0
                    || brand != null
                    || size != null
                    || color != null
                    || sort != null
                    || filterFormSubmitted;

            if (useFilter) {
                products = productDAO.filterProducts(
                        categoryId, size, color, minPrice, maxPrice, brand, sort);
            } else {
                products = productDAO.getAllProducts();
            }

            request.setAttribute("selectedCategoryId", categoryId);
            request.setAttribute("selectedBrand", brand != null ? brand : "");
            request.setAttribute("selectedSort", sort != null ? sort : "");
            if (filterFormSubmitted) {
                request.setAttribute("minPrice", minPrice);
                request.setAttribute("maxPrice", maxPrice);
            }
        }

        List<Category> categories = categoryDAO.getAllCategories();
        List<String> brands = productDAO.getAllBrands();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);

        request.getRequestDispatcher("/WEB-INF/views/products.jsp")
                .forward(request, response);
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return value != null && !value.isBlank()
                    ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return value != null && !value.isBlank()
                    ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
