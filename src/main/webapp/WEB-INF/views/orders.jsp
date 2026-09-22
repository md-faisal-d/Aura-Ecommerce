<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
List<Order> orders = (List<Order>) request.getAttribute("orders");
Map<Integer, List<OrderItem>> orderItemsMap = (Map<Integer, List<OrderItem>>) request.getAttribute("orderItemsMap");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/checkout.css">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <main class="container orders-page">
        <h1 class="page-title">Order History</h1>

        <% if (orders == null || orders.isEmpty()) { %>
            <div class="empty-state">
                <h2>No orders yet</h2>
                <a href="<%=ctx%>/products" class="btn-primary">Start Shopping</a>
            </div>
        <% } else {
            for (Order order : orders) {
                List<OrderItem> items = orderItemsMap.get(order.getId());
        %>
        <article class="order-card">
            <div class="order-header">
                <div>
                    <strong>Order #<%=order.getId()%></strong>
                    <p><%=order.getOrderedAt()%></p>
                </div>
                <span class="order-status <%=order.getOrderStatus().toLowerCase()%>"><%=order.getOrderStatus()%></span>
            </div>
            <p class="order-address"><%=order.getShippingAddress()%>, <%=order.getCity()%> - <%=order.getPincode()%></p>
            <p class="order-payment">Payment: <%=order.getPaymentMethod()%></p>

            <div class="order-items">
                <% if (items != null) {
                    for (OrderItem item : items) { %>
                <div class="checkout-item">
                    <img src="<%= ImageUtil.toUrl(ctx, item.getImageUrl()) %>" alt="">
                    <div>
                        <strong><%=item.getProductName()%></strong>
                        <p>Qty: <%=item.getQuantity()%> · <%=item.getSize()%> / <%=item.getColor()%></p>
                    </div>
                    <span>₹<%=item.getSubtotal()%></span>
                </div>
                <% }} %>
            </div>
            <div class="order-total">Total: <strong>₹<%=order.getTotalAmount()%></strong></div>
        </article>
        <% }} %>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
