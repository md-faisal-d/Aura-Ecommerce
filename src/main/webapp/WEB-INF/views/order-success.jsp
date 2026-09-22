<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Placed - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>
    <main class="container empty-state success-state">
        <div class="success-icon">✓</div>
        <h1>Order Placed Successfully!</h1>
        <p>Thank you for shopping with Aura. Your order is being processed.</p>
        <a href="<%=ctx%>/orders" class="btn-primary">View Orders</a>
        <a href="<%=ctx%>/products" class="btn-ghost">Continue Shopping</a>
    </main>
    <jsp:include page="/components/footer.jsp"/>
</body>
</html>
