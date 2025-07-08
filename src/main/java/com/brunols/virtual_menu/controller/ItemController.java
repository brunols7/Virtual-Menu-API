package com.brunols.virtual_menu.controller;
import com.brunols.virtual_menu.dto.ItemDTO;
import com.brunols.virtual_menu.entity.Categories;
import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.service.CategoriesService;
import com.brunols.virtual_menu.service.ItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Items")
@RestController
@RequestMapping("/v1/items")
public class ItemController {

    private final CategoriesService categoriesService;
    private final ItemsService itemsService;

    public ItemController(CategoriesService categoriesService, ItemsService itemsService) {
        this.categoriesService = categoriesService;
        this.itemsService = itemsService;
    }

    @Operation(summary = "Create a new item")
    @PostMapping()
    public ResponseEntity<String> createItem(@Valid @RequestBody ItemDTO dto) {
        Categories category = categoriesService.getCategoryById(dto.categoryId());
        Items item = new Items(dto, category);
        itemsService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body("Item created with success!");
    }

    @Operation(summary = "Get all items")
    @GetMapping()
    public ResponseEntity<List<Items>> getItems() {
        List<Items> items = itemsService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Get item by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Items> getItemById(@PathVariable Long id) {
        Items item = itemsService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @Operation(summary = "Get all items by category ID")
    @GetMapping("/category/{id}/")
    public ResponseEntity<List<Items>> getItemsByCategory(@PathVariable Long id) {
        List<Items> items = itemsService.getItemsByCategoryId(id);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Update item by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO dto) {
        Categories category = categoriesService.getCategoryById(dto.categoryId());
        Items item = new Items(dto, category);
        itemsService.updateItem(id, item);
        return ResponseEntity.ok("Item updated with success!");
    }

    @Operation(summary = "Delete item by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
