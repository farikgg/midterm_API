package com.midterm_api.service;

import com.midterm_api.dto.CategoryDto;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void getAllCategoriesTest() {
        categoryService.addCategory(new CategoryDto(null, "Name1", "Color1"));
        categoryService.addCategory(new CategoryDto(null, "Name2", "Color2"));

        List<CategoryDto> categoryDtos = categoryService.getAll();

        Assertions.assertNotNull(categoryDtos);
        Assertions.assertNotEquals(0, categoryDtos.size());

        categoryDtos.forEach(catDto -> {
            Assertions.assertNotNull(catDto.getId());
            Assertions.assertNotNull(catDto.getName());
            Assertions.assertNotNull(catDto.getColor());
        });
    }

    @Test
    void getCategoryByIdTest() {
        categoryService.addCategory(new CategoryDto(null, "Name", "Color"));

        List<CategoryDto> allCategories = categoryService.getAll();

        Random random = new Random();
        int randomIndex = random.nextInt(allCategories.size());
        Long randomId = allCategories.get(randomIndex).getId();

        CategoryDto categoryDto = categoryService.getById(randomId);

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(randomId, categoryDto.getId());
        Assertions.assertNotNull(categoryDto.getName());
        Assertions.assertNotNull(categoryDto.getColor());

        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.getById(-1L));
    }

    @Test
    void addCategoryTest() {
        CategoryDto category = new CategoryDto(null, "NewName", "NewColor");

        CategoryDto createdCategory = categoryService.addCategory(category);

        Assertions.assertNotNull(createdCategory);
        Assertions.assertNotNull(createdCategory.getId());
        Assertions.assertEquals("NewName", createdCategory.getName());
        Assertions.assertEquals("NewColor", createdCategory.getColor());
    }

    @Test
    void updateCategoryTest() {
        CategoryDto created = categoryService.addCategory(new CategoryDto(null, "OldName", "OldColor"));

        CategoryDto update = new CategoryDto(null, "UpdatedName", "UpdatedColor");

        categoryService.updateCategory(created.getId(), update);

        CategoryDto updated = categoryService.getById(created.getId());
        Assertions.assertEquals("UpdatedName", updated.getName());
        Assertions.assertEquals("UpdatedColor", updated.getColor());
    }

    @Test
    void deleteCategoryTest() {
        CategoryDto created = categoryService.addCategory(new CategoryDto(null, "DeleteName", "DeleteColor"));
        Long idToDelete = created.getId();

        categoryService.deleteCategory(idToDelete);

        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryService.getById(idToDelete));
    }
}