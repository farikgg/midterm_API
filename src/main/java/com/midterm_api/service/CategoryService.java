package com.midterm_api.service;

import com.midterm_api.dto.CategoryDto;
import com.midterm_api.mapper.CategoryMapper;
import com.midterm_api.models.Category;
import com.midterm_api.models.Post;
import com.midterm_api.repository.CategoryRepository;
import com.midterm_api.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PostRepository postRepository;

    public List<CategoryDto> getAll() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + id + " не найдена"));
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null) {
            throw new RuntimeException("Категория с таким именем уже существует");
        }
        Category category = categoryMapper.toEntity(categoryDto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + id + " не найдена"));

        category.setName(newCategoryDto.getName());
        category.setColor(newCategoryDto.getColor());

        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Категория с ID " + id + " не найдена");
        }
        categoryRepository.deleteById(id);
    }


    @Transactional
    public void addCategoryToPost(Long postId, Long categoryId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + postId + " не найден"));

        checkPostOwnership(post);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + categoryId + " не найдена"));

        // чтобы не добавить дубликат
        if (!post.getCategories().contains(category)) {
            post.getCategories().add(category);
            postRepository.save(post);
        }
    }

    @Transactional
    public void removeCategoryFromPost(Long postId, Long categoryId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + postId + " не найден"));

        // свой ли пост редактирует пользователь
        checkPostOwnership(post);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + categoryId + " не найдена"));

        post.getCategories().remove(category);
        postRepository.save(post);
    }

    // является ли текущий юзер автором поста
    private void checkPostOwnership(Post post) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!post.getUser().getName().equals(currentUsername)) {
            throw new AccessDeniedException("Вы не являетесь автором этого поста");
        }
    }
}
