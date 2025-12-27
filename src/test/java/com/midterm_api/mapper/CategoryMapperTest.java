package com.midterm_api.mapper;

import com.midterm_api.models.Category;
import com.midterm_api.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void convertEntityToDto() {
        Category entity = new Category(1L, "Work", "1234GFG", null);

        CategoryDto dto = categoryMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getColor(), dto.getColor());
    }

    @Test
    void convertDtoToEntity() {
        CategoryDto dto = new CategoryDto(2L, "University", "1234QWER");

        Category entity = categoryMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getColor(), entity.getColor());
    }
}