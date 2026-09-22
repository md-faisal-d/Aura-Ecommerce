<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>
<%@ page import="com.fashionstore.util.SessionWishlist" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
List<Product> products = (List<Product>) request.getAttribute("products");
List<Category> categories = (List<Category>) request.getAttribute("categories");
List<String> brands = (List<String>) request.getAttribute("brands");
String keyword = (String) request.getAttribute("keyword");
int selectedCategoryId = 0;
Object catIdAttr = request.getAttribute("selectedCategoryId");
if (catIdAttr != null) {
    selectedCategoryId = (Integer) catIdAttr;
}
String selectedBrand = (String) request.getAttribute("selectedBrand");
if (selectedBrand == null) selectedBrand = "";
String selectedSort = (String) request.getAttribute("selectedSort");
if (selectedSort == null) selectedSort = "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop - Aura</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=3">
    <link rel="stylesheet" href="<%=ctx%>/assets/css/products.css?v=3">
</head>
<body class="page-bg">
    <jsp:include page="/components/navbar.jsp"/>

    <section class="category-pills container">
        <a href="<%=ctx%>/products" class="pill <%=selectedCategoryId==0 && keyword==null?"active":""%>">All</a>
        <% if (categories != null) {
            for (Category cat : categories) { %>
        <a href="<%=ctx%>/products?categoryId=<%=cat.getId()%>"
           class="pill <%=selectedCategoryId==cat.getId()?"active":""%>"><%=cat.getName()%></a>
        <% }} %>
    </section>

    <main class="shop-layout container">
        <aside class="filter-sidebar">
            <div class="sidebar-header">
                <h3><span>⚙</span> Filters</h3>
                <a href="<%=ctx%>/products" class="reset-link">Reset All</a>
            </div>
            <form action="<%=ctx%>/products" method="get" class="filter-form">
                <input type="hidden" name="filter" value="1">
                
                <div class="filter-group">
                    <label>Search</label>
                    <input type="text" name="keyword" value="<%=keyword!=null?keyword:""%>" placeholder="Keyword...">
                </div>
                
                <div class="filter-group">
                    <label>Category</label>
                    <select name="categoryId">
                        <option value="0" <%=selectedCategoryId==0?"selected":""%>>All Categories</option>
                        <% if (categories != null) {
                            for (Category cat : categories) { %>
                        <option value="<%=cat.getId()%>" <%=selectedCategoryId==cat.getId()?"selected":""%>><%=cat.getName()%></option>
                        <% }} %>
                    </select>
                </div>
                
                <div class="filter-group">
                    <label>Brand</label>
                    <select name="brand">
                        <option value="" <%=selectedBrand.isEmpty()?"selected":""%>>All Brands</option>
                        <% if (brands != null) {
                            for (String brand : brands) { %>
                        <option value="<%=brand%>" <%=selectedBrand.equals(brand)?"selected":""%>><%=brand%></option>
                        <% }} %>
                    </select>
                </div>
                
                <div class="filter-group price-range">
                    <label>Price Range</label>
                    <div class="price-inputs">
                        <input type="number" name="minPrice" placeholder="Min" min="0" value="0">
                        <span class="price-separator">—</span>
                        <input type="number" name="maxPrice" placeholder="Max" value="50000">
                    </div>
                    <div class="price-slider-wrap">
                        <input type="range" class="price-slider" min="0" max="50000" value="5000" oninput="this.nextElementSibling.innerText='Max: ₹' + Number(this.value).toLocaleString()">
                        <span class="price-badge">Max: ₹5,000</span>
                    </div>
                </div>
                
                <div class="filter-group">
                    <label>Sort By</label>
                    <select name="sort">
                        <option value="" <%=selectedSort.isEmpty()?"selected":""%>>Newest</option>
                        <option value="priceLowToHigh" <%=selectedSort.equals("priceLowToHigh")?"selected":""%>>Price: Low to High</option>
                        <option value="priceHighToLow" <%=selectedSort.equals("priceHighToLow")?"selected":""%>>Price: High to Low</option>
                    </select>
                </div>
                
                <div class="filter-actions">
                    <button type="submit" class="btn-apply-filters">Apply Filters</button>
                    <a href="<%=ctx%>/products" class="btn-reset-filters">Reset Filters</a>
                </div>
            </form>
        </aside>

        <section class="products-main">
            <div class="products-header">
                <h1><%
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        out.print("Results for \"" + keyword + "\"");
                    } else if (selectedCategoryId > 0 && categories != null) {
                        String catName = "Category";
                        for (Category c : categories) {
                            if (c.getId() == selectedCategoryId) catName = c.getName();
                        }
                        out.print(catName);
                    } else {
                        out.print("All Products");
                    }
                %></h1>
                <span class="item-count-badge"><%=products!=null?products.size():0%> items</span>
            </div>

            <div class="product-grid">
                <% if (products != null && !products.isEmpty()) {
                    for (Product product : products) {
                        boolean inWishlist = SessionWishlist.contains(session, product.getId());
                %>
                <article class="product-card animate-in">
                    <button type="button" class="wishlist-heart <%=inWishlist?"active":""%>"
                        onclick="toggleWishlist(<%=product.getId()%>)">♥</button>
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
                    <form action="<%=ctx%>/cart" method="post" class="add-cart-form">
                        <input type="hidden" name="productId" value="<%=product.getId()%>">
                        <input type="hidden" name="quantity" value="1">
                        <input type="hidden" name="redirect" value="/products">
                        <button type="submit" class="add-cart-btn">🛒 Add to Cart — ₹<%=product.getPrice()%></button>
                    </form>
                </article>
                <% } } else { %>
                <p class="no-products">No products found matching your criteria. Please try adjusting your filters or search keywords.</p>
                <% } %>
            </div>
        </section>
    </main>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/main.js"></script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
