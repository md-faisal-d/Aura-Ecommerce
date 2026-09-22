package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class ProductDAOTest {

    private ProductDAOImpl productDAO;

    @BeforeEach
    public void setUp() {
        productDAO = new ProductDAOImpl();
    }

    @Test
    public void testGetAllProductsSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<Product> products = productDAO.getAllProducts();
            assertNotNull(products);
            assertFalse(products.isEmpty());

            Product p = products.get(0);
            assertEquals("Test Shirt", p.getName());
            assertEquals("Aura", p.getBrand());
            assertTrue(p.isFeatured());
        }
    }

    @Test
    public void testGetFeaturedProductsSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<Product> featured = productDAO.getFeaturedProducts();
            assertNotNull(featured);
            assertFalse(featured.isEmpty());
            assertTrue(featured.get(0).isFeatured());
            assertEquals("Aura", featured.get(0).getBrand());
        }
    }

    @Test
    public void testGetAllBrandsSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<String> brands = productDAO.getAllBrands();
            assertNotNull(brands);
            assertFalse(brands.isEmpty());
            assertTrue(brands.contains("Aura"));
        }
    }

    @Test
    public void testGetProductByIdNotFound() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            Product product = productDAO.getProductById(9999);
            assertNull(product);
        }
    }

    @Test
    public void testGetVariantsByProductIdSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<ProductVariant> variants = productDAO.getVariantsByProductId(1);
            assertNotNull(variants);
            assertFalse(variants.isEmpty());
            assertEquals("M", variants.get(0).getSize());
            assertEquals(10, variants.get(0).getStock());
        }
    }

    @Test
    public void testSearchProductsNoMatches() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<Product> results = productDAO.searchProducts("nonexistent_item_keyword_123");
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }
}
