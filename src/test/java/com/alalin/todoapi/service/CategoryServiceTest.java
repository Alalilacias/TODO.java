package com.alalin.todoapi.service;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.exception.CategoryAlreadyExistsException;
import com.alalin.todoapi.exception.CategoryNotFoundException;
import com.alalin.todoapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(1L, "Work");
    }

    @Test
    void createCategory_whenCategoryDoesNotExist_savesCategory() {
        when(categoryRepository.existsByName("Work")).thenReturn(false);
        when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);

        Category created = categoryService.createCategory(sampleCategory);

        assertEquals(sampleCategory, created);
        verify(categoryRepository).save(sampleCategory);
    }

    @Test
    void createCategory_whenCategoryExists_throwsException() {
        when(categoryRepository.existsByName("Work")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class, () ->
                categoryService.createCategory(sampleCategory)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAllCategories_returnsList() {
        List<Category> categories = List.of(sampleCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals(sampleCategory, result.get(0));
    }

    @Test
    void getCategoryByName_whenFound_returnsCategory() {
        when(categoryRepository.findByName("Work")).thenReturn(Optional.of(sampleCategory));

        Category found = categoryService.getCategoryByName("Work");

        assertEquals(sampleCategory, found);
    }

    @Test
    void getCategoryByName_whenNotFound_throwsException() {
        when(categoryRepository.findByName("Missing")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.getCategoryByName("Missing")
        );
    }

    @Test
    void deleteCategory_whenFound_deletesCategory() {
        when(categoryRepository.findByName("Work")).thenReturn(Optional.of(sampleCategory));

        categoryService.deleteCategory("Work");

        verify(categoryRepository).delete(sampleCategory);
    }

    @Test
    void deleteCategory_whenNotFound_throwsException() {
        when(categoryRepository.findByName("Missing")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.deleteCategory("Missing")
        );

        verify(categoryRepository, never()).delete(any());
    }
}