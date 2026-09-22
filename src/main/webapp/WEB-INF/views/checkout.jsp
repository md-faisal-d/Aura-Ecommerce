<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.CartItem" %>
<%@ page import="com.fashionstore.model.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
User user = (User) request.getAttribute("user");
List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
BigDecimal cartTotal = (BigDecimal) request.getAttribute("cartTotal");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/checkout.css">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <main class="container checkout-page">
        <h1 class="page-title">Checkout</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert-error"><%=request.getAttribute("error")%></div>
        <% } %>

        <div class="checkout-layout">
            <form action="<%=ctx%>/place-order" method="post" class="checkout-form">
                <h2>Shipping Details</h2>
                <div class="form-grid">
                    <label>Full Name<input type="text" name="name" value="<%=user.getName()%>" readonly></label>
                    <label>Phone<input type="tel" name="phone" value="<%=user.getPhone()%>" required></label>
                    <label class="full">Address<textarea name="address" rows="3" required><%=user.getAddress()%></textarea></label>
                    <label>City<input type="text" name="city" value="<%=user.getCity()%>" required></label>
                    <label>State<input type="text" name="state" value="<%=user.getState()%>" required></label>
                    <label>Pincode<input type="text" name="pincode" value="<%=user.getPincode()%>" required></label>
                </div>

                <h2>Payment</h2>
                <div class="payment-options">
                    <label class="payment-card">
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        <span>Cash on Delivery</span>
                    </label>
                    <label class="payment-card">
                        <input type="radio" name="paymentMethod" value="UPI">
                        <span>UPI</span>
                    </label>
                    <label class="payment-card">
                        <input type="radio" name="paymentMethod" value="Card">
                        <span>Credit / Debit Card</span>
                    </label>
                </div>

                <button type="submit" class="btn-primary btn-block">Place Order</button>
            </form>

            <aside class="checkout-summary">
                <h3>Your Items</h3>
                <% for (CartItem item : cartItems) { %>
                <div class="checkout-item">
                    <img src="<%= ImageUtil.toUrl(ctx, item.getImageUrl()) %>" alt="">
                    <div>
                        <strong><%=item.getProductName()%></strong>
                        <p>Qty: <%=item.getQuantity()%> · ₹<%=item.getPrice()%></p>
                    </div>
                    <span>₹<%=item.getSubtotal()%></span>
                </div>
                <% } %>
                <div class="summary-total">
                    <span>Grand Total</span>
                    <strong>₹<%=cartTotal%></strong>
                </div>
            </aside>
        </div>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
