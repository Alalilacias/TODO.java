package com.alalin.todoapi.service;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.exception.CategoryAlreadyExistsException;
import com.alalin.todoapi.exception.CategoryNotFoundException;
import com.alalin.todoapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException(category.getName());
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + name));
    }

    public void deleteCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + name));
        categoryRepository.delete(category);
    }
}
