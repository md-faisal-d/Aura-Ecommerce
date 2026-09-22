package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

public interface ProductDAO {

    // Get all products
    List<Product> getAllProducts();

    // Get single product
    Product getProductById(int productId);

    // Featured products
    List<Product> getFeaturedProducts();

    // New arrivals
    List<Product> getNewArrivals();

    // Search products
    List<Product> searchProducts(String keyword);

    // Products by category
    List<Product> getProductsByCategory(int categoryId);

    // Filter products
    List<Product> filterProducts(
            int categoryId,
            String size,
            String color,
            double minPrice,
            double maxPrice,
            String brand,
            String sort
    );

    // Product variants
    List<ProductVariant> getVariantsByProductId(int productId);

    // Admin operations (future use)
    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);

    List<String> getAllBrands();
}