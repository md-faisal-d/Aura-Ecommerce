<%@ page import="com.fashionstore.model.User" %>
<%@ page import="com.fashionstore.util.SessionAuth" %>
<%@ page import="com.fashionstore.util.SessionCart" %>
<%@ page import="com.fashionstore.util.SessionWishlist" %>
<%
String ctx = request.getContextPath();
User currentUser = SessionAuth.getUser(session);
int cartCount = SessionCart.getItemCount(session);
int wishlistCount = SessionWishlist.getProductIds(session).size();
String uri = request.getRequestURI();
%>
<header class="navbar">
    <a href="<%=ctx%>/home" class="logo">Aura<span>.</span></a>

    <div class="search-box">
        <form action="<%=ctx%>/products" method="get" class="search-form" autocomplete="off">
            <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
            <input type="text" name="keyword" id="searchInput" class="search-input" placeholder="Search products..." autocomplete="off">
            <div id="searchSuggestions" class="search-suggestions"></div>
        </form>
    </div>

    <nav class="nav-links">
        <a href="<%=ctx%>/home" class="<%=uri.contains("/home")?"nav-active":""%>">Home</a>
        <a href="<%=ctx%>/products" class="<%=uri.contains("/products")?"nav-active":""%>">Shop</a>
        <a href="<%=ctx%>/orders">Orders</a>
        <a href="<%=ctx%>/wishlist" class="nav-icon-link">
            Favourites
            <% if (wishlistCount > 0) { %>
            <span class="badge"><%=wishlistCount%></span>
            <% } %>
        </a>
        <a href="<%=ctx%>/cart" class="nav-icon-link">
            Cart
            <% if (cartCount > 0) { %>
            <span class="badge cart-badge"><%=cartCount%></span>
            <% } %>
        </a>
        <% if (currentUser != null) { %>
            <div class="user-menu">
                <span class="user-avatar"><%= currentUser.getName().substring(0, 1).toUpperCase() %></span>
                <span class="user-name"><%= currentUser.getName() %></span>
            </div>
            <a href="<%=ctx%>/logout">Logout</a>
        <% } else { %>
            <a href="<%=ctx%>/login">Login</a>
            <a href="<%=ctx%>/register" class="btn-nav-register">Register</a>
        <% } %>
    </nav>
</header>
