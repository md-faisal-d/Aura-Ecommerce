// CategoryDAO.java

package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.Category;

public interface CategoryDAO {

    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);
}