package com.midterm_api.service;

import com.midterm_api.dto.PostDto;
import com.midterm_api.mapper.PostMapper;
import com.midterm_api.models.Post;
import com.midterm_api.models.User;
import com.midterm_api.repository.PostRepository;
import com.midterm_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository PostRepository;
    private final UserRepository userRepository;
    private final PostMapper PostMapper;

    public List<PostDto> getAll() {
        return PostMapper.toDtoList(PostRepository.findAll());
    }

    public PostDto getById(Long id) {
        Post postEntity = PostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + id + " не найден"));

        return PostMapper.toDto(postEntity);
    }

    @Transactional
    public PostDto addPost(PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findByName(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + currentUsername));

        Post postEntity = PostMapper.toEntity(postDto);
        postEntity.setUser(user);

        Post savedPost = PostRepository.save(postEntity);
        return PostMapper.toDto(savedPost);
    }

    @Transactional
    public PostDto updatePost(Long id, PostDto newPostDto) {
        Post postEntity = PostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + id + " не найден"));

        // нельзя менять чужие посты
        checkPostOwnership(postEntity);

        postEntity.setTitle(newPostDto.getTitle());
        postEntity.setText(newPostDto.getText());

        Post updatedPost = PostRepository.save(postEntity);
        return PostMapper.toDto(updatedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        Post postEntity = PostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + id + " не найден"));

        // нельзя удалять чужие посты
        checkPostOwnership(postEntity);

        PostRepository.delete(postEntity);
    }

    private void checkPostOwnership(Post post) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!post.getUser().getName().equals(currentUsername)) {
            throw new AccessDeniedException("Вы не являетесь автором этого поста");
        }
    }
}