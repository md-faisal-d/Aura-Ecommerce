package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class CategoryDAOTest {

    private CategoryDAOImpl categoryDAO;

    @BeforeEach
    public void setUp() {
        categoryDAO = new CategoryDAOImpl();
    }

    @Test
    public void testGetAllCategoriesSuccess() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            List<Category> categories = categoryDAO.getAllCategories();
            assertNotNull(categories);
            assertFalse(categories.isEmpty());
        }
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            Category category = categoryDAO.getCategoryById(99999);
            assertNull(category);
        }
    }
}
