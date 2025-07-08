package com.brunols.virtual_menu.controller;
import com.brunols.virtual_menu.dto.CategoryDTO;
import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories")
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoriesService categoriesService;

    public CategoryController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Create a new category")
    @PostMapping()
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO dto) {
        Categories category = new Categories(dto);
        categoriesService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created with success!");
    }

    @Operation(summary = "Get all categories")
    @GetMapping()
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = categoriesService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable Long id) {
        Categories category = categoriesService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Update category by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        Categories category = new Categories(dto);
        categoriesService.updateCategory(id, category);
        return ResponseEntity.ok("Category updated with success!");
    }

    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
