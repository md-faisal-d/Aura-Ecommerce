<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.ProductVariant" %>
<%@ page import="com.fashionstore.model.Review" %>
<%@ page import="com.fashionstore.util.SessionAuth" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
Product product = (Product) request.getAttribute("product");
List<ProductVariant> variants = (List<ProductVariant>) request.getAttribute("variants");
List<Review> reviews = (List<Review>) request.getAttribute("reviews");
double averageRating = (Double) request.getAttribute("averageRating");
boolean inWishlist = (Boolean) request.getAttribute("inWishlist");
boolean loggedIn = SessionAuth.getUser(session) != null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%=product.getName()%> - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/product-details.css">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <main class="container product-detail-page">
        <div class="product-details">
            <div class="details-image-wrap">
                <img class="details-image" src="<%= ImageUtil.toUrl(ctx, product.getImageUrl()) %>" alt="<%=product.getName()%>">
            </div>
            <div class="details-info">
                <p class="details-brand"><%=product.getBrand()%></p>
                <h1 class="details-name"><%=product.getName()%></h1>
                <div class="rating-display">
                    <span class="stars"><%=String.format("%.1f", averageRating)%> ★</span>
                    <span>(<%=reviews!=null?reviews.size():0%> reviews)</span>
                </div>
                <div class="details-price">₹<%=product.getPrice()%></div>
                <p class="details-description"><%=product.getDescription()%></p>

                <form action="<%=ctx%>/cart" method="post" class="add-cart-detail-form">
                    <input type="hidden" name="productId" value="<%=product.getId()%>">
                    <input type="hidden" name="quantity" value="1">
                    <label>Select Variant</label>
                    <select name="variantId" required>
                        <% for (ProductVariant variant : variants) { %>
                        <option value="<%=variant.getId()%>">
                            <%=variant.getSize()%> / <%=variant.getColor()%> (Stock: <%=variant.getStock()%>)
                        </option>
                        <% } %>
                    </select>
                    <button type="submit" class="btn-primary add-cart-btn">Add to Cart</button>
                </form>

                <form action="<%=ctx%>/wishlist" method="post">
                    <input type="hidden" name="productId" value="<%=product.getId()%>">
                    <input type="hidden" name="action" value="<%=inWishlist?"remove":"add"%>">
                    <input type="hidden" name="redirect" value="/product?id=<%=product.getId()%>">
                    <button type="submit" class="btn-ghost wishlist-btn"><%=inWishlist?"Remove from":"Add to"%> Wishlist</button>
                </form>
            </div>
        </div>

        <section class="reviews-section">
            <h2>Customer Reviews</h2>
            <% if (reviews != null && !reviews.isEmpty()) {
                for (Review review : reviews) { %>
            <article class="review-card">
                <div class="review-header">
                    <strong><%=review.getUserName()!=null?review.getUserName():"User"%></strong>
                    <span class="review-stars"><%=review.getRating()%> ★</span>
                </div>
                <p><%=review.getReviewText()%></p>
            </article>
            <% }} else { %>
            <p>No reviews yet. Be the first!</p>
            <% } %>

            <% if (loggedIn) { %>
            <form action="<%=ctx%>/review" method="post" class="review-form">
                <input type="hidden" name="productId" value="<%=product.getId()%>">
                <label>Your Rating</label>
                <select name="rating" required>
                    <option value="5">5 - Excellent</option>
                    <option value="4">4 - Good</option>
                    <option value="3">3 - Average</option>
                    <option value="2">2 - Poor</option>
                    <option value="1">1 - Bad</option>
                </select>
                <label>Your Review</label>
                <textarea name="reviewText" rows="4" placeholder="Share your experience..." required></textarea>
                <button type="submit" class="btn-primary">Submit Review</button>
            </form>
            <% } else { %>
            <p><a href="<%=ctx%>/login">Login</a> to write a review.</p>
            <% } %>
        </section>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
