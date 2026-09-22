<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Category" %>
<%@ page import="com.fashionstore.util.ImageUtil" %>
<%
String ctx = request.getContextPath();
List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aura — House of Modern Luxury</title>
    <link rel="stylesheet" href="<%=ctx%>/assets/css/style.css?v=3">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;0,700;1,400&family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">
</head>
<body class="page-bg luxury-theme">
    <jsp:include page="/components/navbar.jsp"/>

    <!-- HERO SECTION -->
    <section class="luxury-hero">
        <div class="luxury-hero-overlay"></div>
        <div class="luxury-hero-content animate-in">
            <span class="luxury-subtitle">HAUTE COUTURE & ESSENTIAL ELEGANCE</span>
            <h1 class="luxury-title">THE HOUSE OF <span>AURA</span></h1>
            <p class="luxury-lead">Where timeless design meets artisan perfection. Crafting refined, sustainable wardrobe foundations for those who speak with quiet confidence.</p>
            <div class="luxury-hero-actions">
                <a href="<%=ctx%>/products" class="luxury-btn primary">EXPLORE ATELIER</a>
                <a href="#our-story" class="luxury-btn secondary">DISCOVER OUR STORY</a>
            </div>
        </div>
    </section>

    <!-- BRAND STORY SECTION -->
    <section id="our-story" class="section luxury-story-section container">
        <div class="story-grid">
            <div class="story-text animate-in">
                <span class="section-tag">OUR PHILOSOPHY</span>
                <h2 class="story-heading">Elegance is an Attitude, Not a Season</h2>
                <div class="story-divider"></div>
                <p>Founded on the belief that luxury should be felt rather than shouted, <strong>Aura</strong> creates timeless garments engineered with meticulous precision and hand-selected natural textiles.</p>
                <p>We reject the transient cycles of fast fashion. Instead, every piece in our collection represents months of architectural tailoring, subtle nuance, and uncompromising quality — resulting in wardrobe anchors designed to last a lifetime.</p>
                <div class="story-stats">
                    <div class="stat-item">
                        <span class="stat-number">100%</span>
                        <span class="stat-label">Organic & Ethically Sourced</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number">Precision</span>
                        <span class="stat-label">Artisan Hand Tailoring</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number">Timeless</span>
                        <span class="stat-label">Seasonless Architecture</span>
                    </div>
                </div>
            </div>
            <div class="story-visual animate-in">
                <div class="luxury-image-card">
                    <img src="<%= ImageUtil.toUrl(ctx, "assets/images/products/4.png") %>?v=4" alt="Men's White Oxford Shirt" class="luxury-story-img">
                    <div class="image-caption">
                        <span>ATELIER EDITION N° 04</span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- PILLARS OF DISTINCTION -->
    <section class="section luxury-pillars-section">
        <div class="container">
            <div class="section-header text-center">
                <span class="section-tag">CURATED STANDARDS</span>
                <h2 class="section-title luxury-font">PILLARS OF DISTINCTION</h2>
                <p class="section-desc">Four core principles defining every thread woven under the House of Aura.</p>
            </div>

            <div class="pillars-grid">
                <div class="pillar-card animate-in">
                    <div class="pillar-icon">🏛️</div>
                    <h3>HERITAGE TAILORING</h3>
                    <p>Rooted in classical garment construction, our master patternmakers sculpt each silhouette to elevate form and movement naturally.</p>
                </div>
                <div class="pillar-card animate-in">
                    <div class="pillar-icon">🌿</div>
                    <h3>SUSTAINABLE LUXURY</h3>
                    <p>We commit to low-impact dyeing methods, organic long-staple cottons, and closed-loop ethical manufacturing partners.</p>
                </div>
                <div class="pillar-card animate-in">
                    <div class="pillar-icon">✨</div>
                    <h3>MINIMALIST SOPHISTICATION</h3>
                    <p>Subtle details, clean geometry, and rich muted palettes crafted to harmonize seamlessly with your personal style statement.</p>
                </div>
                <div class="pillar-card animate-in">
                    <div class="pillar-icon">💎</div>
                    <h3>WHITE-GLOVE SERVICE</h3>
                    <p>From seamless digital styling assistance to priority boutique shipping, experience fashion tailored around your life.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- ATELIER COLLECTIONS (CATEGORIES ONLY) -->
    <section class="section container">
        <div class="section-header text-center">
            <span class="section-tag">CURATED CATEGORIES</span>
            <h2 class="section-title luxury-font">THE ATELIER COLLECTIONS</h2>
            <p class="section-desc">Select a category to browse our full catalog.</p>
        </div>

        <div class="luxury-category-grid">
            <% if (categories != null) {
                for (Category cat : categories) { %>
            <a href="<%=ctx%>/products?categoryId=<%=cat.getId()%>" class="luxury-category-tile animate-in">
                <div class="tile-content">
                    <h3><%=cat.getName()%></h3>
                    <span class="tile-link">EXPLORE COLLECTION &rarr;</span>
                </div>
            </a>
            <% }} %>
        </div>
    </section>

    <!-- BRAND BANNER / QUOTE -->
    <section class="luxury-quote-banner">
        <div class="quote-content container text-center">
            <p class="quote-text">“Fashion changes, but style endures. Aura was created to celebrate the quiet power of personal elegance.”</p>
            <span class="quote-author">— HOUSE OF AURA CREATIVE DIRECTOR</span>
            <div class="quote-cta">
                <a href="<%=ctx%>/products" class="luxury-btn primary gold">VIEW ALL CREATIONS</a>
            </div>
        </div>
    </section>

    <!-- NEWSLETTER / PRIVATE CLIENT CLUB -->
    <section class="section luxury-newsletter container">
        <div class="newsletter-card text-center animate-in">
            <span class="section-tag">BY INVITATION</span>
            <h2 class="luxury-font">THE AURA PRIVATE CLUB</h2>
            <p>Subscribe for private collection previews, exclusive sartorial releases, and luxury lifestyle journals.</p>
            <form class="newsletter-form" onsubmit="event.preventDefault(); alert('Thank you for subscribing to Aura Private Club.');">
                <input type="email" placeholder="Enter your email address" required>
                <button type="submit" class="luxury-btn primary">REQUEST INVITATION</button>
            </form>
        </div>
    </section>

    <jsp:include page="/components/footer.jsp"/>
    <script>window.APP_CONTEXT = '<%=ctx%>';</script>
    <script src="<%=ctx%>/assets/js/search.js"></script>
</body>
</html>
