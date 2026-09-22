package com.fashionstore.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.util.ImageUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {

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
            throws IOException {

        String keyword = request.getParameter("q");

        if (keyword == null || keyword.trim().length() < 2) {
            response.setContentType("application/json");
            response.getWriter().print("[]");
            return;
        }

        List<Product> products = productDAO.searchProducts(keyword.trim());

        if (products.size() > 8) {
            products = products.subList(0, 8);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder("[");
        String ctx = request.getContextPath();

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{");
            json.append("\"id\":").append(p.getId());
            json.append(",\"name\":").append(jsonString(p.getName()));
            json.append(",\"brand\":").append(jsonString(p.getBrand()));
            json.append(",\"price\":").append(p.getPrice());
            json.append(",\"imageUrl\":").append(jsonString(ImageUtil.toUrl(ctx, p.getImageUrl())));
            json.append(",\"url\":").append(jsonString(ctx + "/product?id=" + p.getId()));
            json.append("}");
        }

        json.append("]");
        out.print(json);
        out.flush();
    }

    private String jsonString(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
