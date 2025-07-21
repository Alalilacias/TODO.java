package com.alalin.todoapi.controller;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.exception.ApiError;
import com.alalin.todoapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Category already exists", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Category.class))
            )
            @RequestBody Category category
    ) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of categories")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get a category by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{name}")
    public ResponseEntity<Category> getCategoryByName(
            @Parameter(description = "Name of the category", required = true)
            @PathVariable String name
    ) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @Operation(summary = "Delete a category by name")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Name of the category to delete", required = true)
            @PathVariable String name
    ) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}