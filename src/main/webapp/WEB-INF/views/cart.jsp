<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
BigDecimal cartTotal = (BigDecimal) request.getAttribute("cartTotal");
if (cartTotal == null) cartTotal = BigDecimal.ZERO;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/cart.css">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <main class="container cart-page">
        <h1 class="page-title">Your Cart</h1>

        <% if (cartItems == null || cartItems.isEmpty()) { %>
            <div class="empty-state">
                <h2>Your cart is empty</h2>
                <p>Add something you love from our collection.</p>
                <a href="<%=ctx%>/products" class="btn-primary">Shop Products</a>
            </div>
        <% } else { %>
            <div class="cart-layout">
                <div class="cart-items" id="cartItems">
                    <% for (CartItem item : cartItems) { %>
                    <article class="cart-card" data-variant-id="<%=item.getVariantId()%>">
                        <img src="<%= ImageUtil.toUrl(ctx, item.getImageUrl()) %>" alt="<%=item.getProductName()%>">
                        <div class="cart-info">
                            <h2><%=item.getProductName()%></h2>
                            <p class="cart-meta">Size: <%=item.getSize()%> &bull; Color: <%=item.getColor()%></p>
                            <p class="cart-unit-price">₹<%=item.getPrice()%> each</p>
                            <div class="qty-controls">
                                <button type="button" class="qty-btn" data-action="decrement" data-variant="<%=item.getVariantId()%>">−</button>
                                <span class="qty-value" id="qty-<%=item.getVariantId()%>"><%=item.getQuantity()%></span>
                                <button type="button" class="qty-btn" data-action="increment" data-variant="<%=item.getVariantId()%>">+</button>
                                <button type="button" class="remove-btn" data-action="remove" data-variant="<%=item.getVariantId()%>">Remove</button>
                            </div>
                        </div>
                        <div class="cart-line-total" id="subtotal-<%=item.getVariantId()%>">
                            ₹<%=item.getSubtotal()%>
                        </div>
                    </article>
                    <% } %>
                </div>

                <aside class="cart-summary">
                    <h3>Order Summary</h3>
                    <div class="summary-row">
                        <span>Products</span>
                        <span id="summaryCount"><%=cartItems.size()%></span>
                    </div>
                    <div class="summary-row">
                        <span>Total pieces</span>
                        <span><%=request.getAttribute("cartCount")%></span>
                    </div>
                    <div class="summary-row total-row">
                        <span>Grand Total</span>
                        <strong id="grandTotal">₹<%=cartTotal%></strong>
                    </div>
                    <a href="<%=ctx%>/checkout" class="btn-primary btn-block">Proceed to Checkout</a>
                    <a href="<%=ctx%>/products" class="btn-ghost btn-block">Continue Shopping</a>
                </aside>
            </div>
        <% } %>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/cart.js"></script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
