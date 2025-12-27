package com.midterm_api.mapper;

import com.midterm_api.models.Post;
import com.midterm_api.dto.PostDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    void convertEntityToDto() {
        Post entity = new Post(1L, "Go to the gym", "Biceps 50kg x 10", null, null );

        PostDto dto = postMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTitle(), dto.getTitle());
        assertEquals(entity.getText(), dto.getText());
    }

    @Test
    void convertDtoToEntity() {
        PostDto dto = new PostDto(2L, "Try to do backflip", "Stay alive (optional)");

        Post entity = postMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getText(), entity.getText());
    }
}
