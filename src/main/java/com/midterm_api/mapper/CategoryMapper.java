package com.midterm_api.mapper;

import com.midterm_api.dto.CategoryDto;
import com.midterm_api.models.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
    List<CategoryDto> toDtoList(List<Category> categoryEntityList);
}
