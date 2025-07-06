package com.brunols.virtual_menu.controller;

import com.brunols.virtual_menu.dto.CategoryDTO;
import com.brunols.virtual_menu.dto.ItemDTO;
import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.service.CategoriesService;
import com.brunols.virtual_menu.service.ItemsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CategoriesService categoriesService;
    private final ItemsService itemsService;

    public CatalogController(CategoriesService categoriesService, ItemsService itemsService) {
        this.categoriesService = categoriesService;
        this.itemsService = itemsService;
    }


    @PostMapping("/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO dto){
        Categories category = new Categories(dto);
        categoriesService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created with success!");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = categoriesService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable Long id) {
        Categories category = categoriesService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/items")
    public ResponseEntity<String> createItem(@Valid @RequestBody ItemDTO dto){
        Categories category = categoriesService.getCategoryById(dto.categoryId());
        Items item = new Items(dto, category);
        itemsService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body("Item created with success!");
    }

    @GetMapping("/items")
    public ResponseEntity<List<Items>> getItems(){
        List<Items> items = itemsService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Items> getItemById(@PathVariable Long id) {
        Items item = itemsService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/categories/{id}/items")
    public ResponseEntity<List<Items>> getItemsByCategory(@PathVariable Long id) {
        List<Items> items = itemsService.getItemsByCategoryId(id);
        return ResponseEntity.ok(items);
    }


    @PatchMapping("/categories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto){
        Categories category = new Categories(dto);
        categoriesService.updateCategory(id, category);
        return ResponseEntity.ok("Category updated with success!");
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO dto){
        Categories category = categoriesService.getCategoryById(dto.categoryId());
        Items item = new Items(dto, category);
        itemsService.updateItem(id, item);
        return ResponseEntity.ok("Item updated with success!");
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        itemsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
