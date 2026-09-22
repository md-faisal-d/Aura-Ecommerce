<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/login.css">
</head>
<body class="page-bg auth-page-wrap">
    <jsp:include page="/components/navbar.jsp"/>

    <div class="auth-page">
        <div class="auth-card auth-card-wide">
            <h2>Create account</h2>
            <p>Join Aura for a smoother shopping experience</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert-error"><%=request.getAttribute("error")%></div>
            <% } %>

            <form action="<%=ctx%>/register" method="post" class="auth-form">
                <input type="text" name="name" placeholder="Full Name" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="tel" name="phone" placeholder="Phone" required>
                <input type="password" name="password" placeholder="Password" required>
                <textarea name="address" placeholder="Address" rows="2" required></textarea>
                <div class="form-row form-row-2">
                    <input type="text" name="city" placeholder="City" required>
                    <input type="text" name="state" placeholder="State" required>
                </div>
                <input type="text" name="pincode" placeholder="Pincode" maxlength="10" required>
                <button type="submit" class="btn-primary btn-block">Register</button>
            </form>

            <p class="auth-switch">Have an account? <a href="<%=ctx%>/login">Login</a></p>
        </div>
    </div>
</body>
</html>
