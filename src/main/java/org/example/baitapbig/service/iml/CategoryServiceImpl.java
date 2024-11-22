package org.example.baitapbig.service.iml;

import org.example.baitapbig.model.Category;
import org.example.baitapbig.repository.CategoryRepository;
import org.example.baitapbig.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    @Override
    public Category getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }
    @Override
    public Boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(category)) {
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }
}
