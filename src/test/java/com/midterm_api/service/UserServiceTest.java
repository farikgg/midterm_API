package com.midterm_api.service;

import com.midterm_api.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService UserService;

    @Test
    void getAllUsersTest() {
        UserService.addUser(new UserDto(null, "User1", "Pass1"));
        UserService.addUser(new UserDto(null, "User2", "Pass2"));

        List<UserDto> userDtos = UserService.getAll();

        Assertions.assertNotNull(userDtos);
        Assertions.assertTrue(userDtos.size() >= 2);

        userDtos.forEach(userDto -> {
            Assertions.assertNotNull(userDto.getId());
            Assertions.assertNotNull(userDto.getName());
        });
    }

    @Test
    void getUserByIdTest() {
        UserService.addUser(new UserDto(null, "TargetUser", "TargetPass"));

        List<UserDto> allUsers = UserService.getAll();

        Long targetId = allUsers.stream()
                .filter(u -> "TargetUser".equals(u.getName()))
                .findFirst()
                .orElseThrow()
                .getId();

        UserDto userDto = UserService.getById(targetId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(targetId, userDto.getId());
        Assertions.assertEquals("TargetUser", userDto.getName()); // Исправлено

        Assertions.assertThrows(EntityNotFoundException.class, () -> UserService.getById(-1L));
    }

    @Test
    void addUserTest() {
        UserDto user = new UserDto(null, "NewUser", "NewPassword");

        UserDto createdUser = UserService.addUser(user);

        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals("NewUser", createdUser.getName());
    }

    @Test
    @WithMockUser(username = "OldName")
    void updateUserTest() {
        UserDto created = UserService.addUser(new UserDto(null, "OldName", "OldPassword"));

        UserDto update = new UserDto(null, "OldName", "UpdatedPassword");

        UserDto updated = UserService.updateUser(created.getId(), update);

        UserDto result = UserService.getById(created.getId());
        Assertions.assertEquals("OldName", result.getName());
        Assertions.assertNotNull(result.getPassword());
    }

    @Test
    @WithMockUser(username = "DeleteUser")
    void deleteUserTest() {
        UserDto created = UserService.addUser(new UserDto(null, "DeleteUser", "Password"));
        Long idToDelete = created.getId();

        UserService.deleteUser(idToDelete);

        Assertions.assertThrows(EntityNotFoundException.class, () -> UserService.getById(idToDelete));
    }
}