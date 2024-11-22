package org.example.baitapbig.service;

import org.example.baitapbig.model.Category;

import java.util.List;

public interface CategoryService {
    public Category getCategoryById(int id);
    public Category saveCategory(Category category);
    public Boolean existCategory(String name);
    public List<Category> getAllCategory();
    public Boolean deleteCategory(int id);

}
