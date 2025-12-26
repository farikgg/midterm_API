package com.midterm_api.controller;

import com.midterm_api.dto.CategoryDto;
import com.midterm_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto created = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updated = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // связь many to many
    @PostMapping("/add-to-post/{postId}/{categoryId}")
    public ResponseEntity<String> addCategoryToPost(@PathVariable Long postId, @PathVariable Long categoryId) {
        categoryService.addCategoryToPost(postId, categoryId);
        return ResponseEntity.ok("Категория успешно добавлена к посту");
    }

    @DeleteMapping("/remove-from-post/{postId}/{categoryId}")
    public ResponseEntity<String> removeCategoryFromPost(@PathVariable Long postId, @PathVariable Long categoryId) {
        categoryService.removeCategoryFromPost(postId, categoryId);
        return ResponseEntity.ok("Категория успешно удалена из поста");
    }
}