<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();
String redirect = request.getParameter("redirect");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=2">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/login.css">
</head>
<body class="page-bg auth-page-wrap">
    <jsp:include page="/components/navbar.jsp"/>

    <div class="auth-page">
        <div class="auth-card">
            <h2>Welcome back</h2>
            <p>Sign in to continue shopping</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert-error"><%=request.getAttribute("error")%></div>
            <% } %>

            <form action="<%=ctx%>/login" method="post" class="auth-form">
                <% if (redirect != null) { %>
                <input type="hidden" name="redirect" value="<%=redirect%>">
                <% } %>
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit" class="btn-primary btn-block">Login</button>
            </form>

            <p class="auth-switch">No account? <a href="<%=ctx%>/register">Register</a></p>
            <p class="auth-hint">Demo: faisal@gmail.com / 123456</p>
        </div>
    </div>
</body>
</html>
