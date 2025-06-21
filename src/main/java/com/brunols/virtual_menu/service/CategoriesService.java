package com.brunols.virtual_menu.service;

import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.repository.CategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoriesService {
    private final CategoriesRepository repository;

    public CategoriesService(CategoriesRepository repository){
        this.repository = repository;
    }

    public Categories saveCategory(Categories category){
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (!StringUtils.hasText(category.getName())) {
            throw new IllegalArgumentException("Category name must not be empty");
        }
        boolean exists = repository.existsByNameIgnoreCase(category.getName());
        if (exists) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }

        return repository.save(category);
    }

    public Categories updateCategory(Long id, Categories updatedCategory) {
        Categories existingCategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        if (!StringUtils.hasText(updatedCategory.getName())) {
            throw new IllegalArgumentException("Category name must not be empty");
        }

        boolean exists = repository.existsByNameIgnoreCase(updatedCategory.getName());
        if (exists && !existingCategory.getName().equalsIgnoreCase(updatedCategory.getName())) {
            throw new IllegalArgumentException("Category with name '" + updatedCategory.getName() + "' already exists");
        }

        existingCategory.setName(updatedCategory.getName());
        return repository.save(existingCategory);
    }

    public String deleteCategory(Long id){
        Categories existingCategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id : " + id));

        String categoryName = existingCategory.getName();
        repository.delete(existingCategory);

        return categoryName;
    }

    public Categories getCategoryById(Long id){

        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}
