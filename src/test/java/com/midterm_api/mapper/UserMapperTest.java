package com.midterm_api.mapper;

import com.midterm_api.models.User;
import com.midterm_api.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void convertEntityToDto() {
        User entity = new User(1L, "Rafi", "1234", null, null);

        UserDto dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getPassword(), dto.getPassword());
    }

    @Test
    void convertDtoToEntity() {
        UserDto dto = new UserDto(2L, "Meow", "1234");

        User entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getPassword(), entity.getPassword());
    }
}
