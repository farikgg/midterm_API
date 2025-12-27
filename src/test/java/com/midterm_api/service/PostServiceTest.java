package com.midterm_api.service;

import com.midterm_api.dto.PostDto;
import com.midterm_api.models.User;
import com.midterm_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository UserRepository;

    @BeforeEach
    void setUp() {
        if (!UserRepository.existsByName("testuser")) {
            User user = new User();
            user.setName("testuser");
            user.setPassword("password");
            UserRepository.save(user);
        }
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAllPostTest() {
        postService.addPost(new PostDto(null, "Title1", "Text1"));
        postService.addPost(new PostDto(null, "Title2", "Text2"));

        List<PostDto> PostDtos = postService.getAll();

        Assertions.assertNotNull(PostDtos);
        Assertions.assertTrue(PostDtos.size() >= 2);

        PostDtos.forEach(postDto -> {
            Assertions.assertNotNull(postDto.getId());
            Assertions.assertNotNull(postDto.getTitle());
            Assertions.assertNotNull(postDto.getText());
        });
    }

    @Test
    @WithMockUser(username = "testuser")
    void getPostByIdTest() {
        postService.addPost(new PostDto(null, "TitleDto", "TextDto"));

        List<PostDto> allPost = postService.getAll();

        Random random = new Random();
        int randomIndex = random.nextInt(allPost.size());
        Long randomId = allPost.get(randomIndex).getId();

        PostDto postDto = postService.getById(randomId);

        Assertions.assertNotNull(postDto);
        Assertions.assertEquals(randomId, postDto.getId());
        Assertions.assertNotNull(postDto.getTitle());
        Assertions.assertNotNull(postDto.getText());

        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.getById(-1L));
    }

    @Test
    @WithMockUser(username = "testuser")
    void addPostTest() {
        PostDto post = new PostDto(null, "NewTitle", "NewText");

        PostDto createdPost = postService.addPost(post);

        Assertions.assertNotNull(createdPost);
        Assertions.assertNotNull(createdPost.getId());
        Assertions.assertEquals("NewTitle", createdPost.getTitle());
        Assertions.assertEquals("NewText", createdPost.getText());
    }

    @Test
    @WithMockUser(username = "testuser")
    void updatePostTest() {
        PostDto created = postService.addPost(new PostDto(null, "OldTitle", "OldText"));

        PostDto update = new PostDto(null, "UpdatedTitle", "UpdatedText");

        PostDto updated = postService.updatePost(created.getId(), update);

        Assertions.assertEquals("UpdatedTitle", updated.getTitle());
        Assertions.assertEquals("UpdatedText", updated.getText());
    }

    @Test
    @WithMockUser(username = "testuser")
    void deletePostTest() {
        PostDto created = postService.addPost(new PostDto(null, "DeleteTitle", "DeleteText"));
        Long idToDelete = created.getId();

        postService.deletePost(idToDelete);

        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.getById(idToDelete));
    }
}