# 🌟 Aura — Luxury Fashion E-Commerce Store

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.1-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-lightgrey.svg)
![CSS3](https://img.shields.io/badge/Style-Modern%20Luxury-gold.svg)

**Aura** is a full-stack e-commerce web application engineered with Java Servlets, JSP, and MySQL. It offers a high-end luxury brand shopping experience featuring brand storytelling, category-based product exploration, real-time product filtering, a session-based shopping cart, wishlist management, and order handling.

---

## 📸 Key Features

### 🏛️ 1. Brand Story & Luxury Homepage
- **Editorial Brand Landing**: Dedicated brand story showcasing Aura's heritage, sustainability standards, and artisan craftsmanship.
- **Atelier Collections Grid**: 3-column symmetrical category showcase (`Men`, `Women`, `Kids`, `Ethnic Wear`, `Footwear`, `Accessories`).
- **Private Club Membership**: Interactive invitation form for exclusive client updates.

### 🛍️ 2. Dynamic Product Catalog & Filtering
- **Multi-Criteria Filter Sidebar**: Live keyword search, category selection, brand dropdowns, interactive price range slider with badge display, and sorting (Newest, Price: Low to High, Price: High to Low).
- **Sticky & Responsive Sidebar**: Non-blocking sticky navigation with smooth scrolling across viewports.
- **Clean 1:1 Product Asset Mapping**: 24 curated catalog items mapped to high-resolution product imagery.

### 🛒 3. Shopping Cart & Wishlist
- **Session-Based Cart**: Add, update quantities, and remove items with dynamic subtotal calculations.
- **Wishlist Toggle**: Frosted-glass quick heart toggles for item favoriting across shop grids.
- **Robust Variant Handling**: Intelligent fallbacks for product size and color variants.

### 🔐 4. Authentication & Security
- **User Registration & Login**: Account creation with secure password hashing (`password_hash`) and user address persistence.
- **Sanitized Credentials**: Environment variable configuration (`DB_PASSWORD`) ensuring zero hardcoded secrets in source control.
- **SQL Injection Prevention**: Parameterized queries across all DAO implementation layers.

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Jakarta Servlet API (Tomcat 10+), JavaServer Pages (JSP)
- **Database**: MySQL 8.0 (JDBC Connection Pooling)
- **Frontend**: HTML5, CSS3 (Custom CSS Grid & Flexbox layouts, Cormorant Garamond serif typography), Vanilla JavaScript
- **Server**: Apache Tomcat 10.1
- **Architecture**: MVC (Model-View-Controller) with DAO pattern

---

## 📁 Directory Structure

```text
FashionStore/
├── database_schema.sql          # Full database export (tables, 24 products, seed data)
├── src/main/java/
│   └── com/fashionstore/
│       ├── controller/          # Servlets (HomeServlet, ProductDetailsServlet, etc.)
│       ├── dao/                 # DAO interfaces & JDBC implementations
│       ├── model/               # Data Models (Product, Category, User, Variant)
│       └── util/                # Utilities (DBConnection, SessionCart, ImageUtil)
├── src/main/webapp/
│   ├── assets/
│   │   ├── css/                 # style.css (global/luxury), products.css (shop layout)
│   │   ├── js/                  # main.js, search.js
│   │   └── images/products/     # Product images (1.png - 24.png)
│   ├── components/              # navbar.jsp, footer.jsp
│   ├── WEB-INF/views/           # home.jsp, products.jsp, cart.jsp, login.jsp, etc.
│   └── index.jsp                # Root entry point
└── pom.xml / build manifests    # Build configuration
```

---

## ⚙️ Database & Local Setup

### 1. Database Initialization
Import `database_schema.sql` into your local MySQL server:

```sql
CREATE DATABASE fashion_store;
USE fashion_store;
SOURCE database_schema.sql;
```

### 2. Set Environment Variable
Configure your MySQL root password as an environment variable (`DB_PASSWORD`):

- **Windows (PowerShell)**:
  ```powershell
  $env:DB_PASSWORD="your_mysql_password"
  ```
- **Linux / macOS**:
  ```bash
  export DB_PASSWORD="your_mysql_password"
  ```

### 3. Deploy to Tomcat
1. Compile Java source files using target JDK 17:
   ```bash
   javac --release 17 -cp "path/to/tomcat/lib/*" -d target/classes src/main/java/com/fashionstore/**/*.java
   ```
2. Deploy `src/main/webapp` and compiled classes (`target/classes`) to Tomcat's `webapps/FashionStore` directory.
3. Access the application in your browser: `http://localhost:8080/FashionStore/home`

---

## 🛡️ License

Distributed under the MIT License. See `LICENSE` for more information.
