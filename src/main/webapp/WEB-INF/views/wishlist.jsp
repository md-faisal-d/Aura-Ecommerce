<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
List<Product> products = (List<Product>) request.getAttribute("products");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wishlist - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/products.css">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <main class="container">
        <h1 class="page-title">Your Favourites</h1>

        <% if (products == null || products.isEmpty()) { %>
            <div class="empty-state">
                <h2>No favourites yet</h2>
                <a href="<%=ctx%>/products" class="btn-primary">Browse Products</a>
            </div>
        <% } else { %>
            <div class="product-grid">
                <% for (Product product : products) { %>
                <article class="product-card">
                    <form action="<%=ctx%>/wishlist" method="post" class="wishlist-remove-form">
                        <input type="hidden" name="productId" value="<%=product.getId()%>">
                        <input type="hidden" name="action" value="remove">
                        <button type="submit" class="wishlist-heart active">♥</button>
                    </form>
                    <a href="<%=ctx%>/product?id=<%=product.getId()%>" class="product-link">
                        <div class="product-image-wrap">
                            <img src="<%= ImageUtil.toUrl(ctx, product.getImageUrl()) %>" alt="<%=product.getName()%>">
                        </div>
                        <div class="product-info">
                            <p class="product-brand"><%=product.getBrand()%></p>
                            <h3 class="product-name"><%=product.getName()%></h3>
                            <p class="product-price">₹<%=product.getPrice()%></p>
                        </div>
                    </a>
                    <form action="<%=ctx%>/cart" method="post">
                        <input type="hidden" name="productId" value="<%=product.getId()%>">
                        <input type="hidden" name="quantity" value="1">
                        <button type="submit" class="add-cart-btn">Add to Cart</button>
                    </form>
                </article>
                <% } %>
            </div>
        <% } %>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
