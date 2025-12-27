package com.midterm_api.service;

import com.midterm_api.dto.UserDto;
import com.midterm_api.mapper.UserMapper;
import com.midterm_api.models.Permission;
import com.midterm_api.models.User;
import com.midterm_api.repository.PermissionRepository;
import com.midterm_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public List<UserDto> getAll(){
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getById(Long id) {
        User userEntity = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        // существует ли юзер
        if (userRepository.existsByName(userDto.getName())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User userEntity = userMapper.toEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Логика ролей
        Permission roleUser = permissionRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Permission(null, "ROLE_USER");
            roleUser = permissionRepository.save(roleUser);
        }

        userEntity.setPermissions(new ArrayList<>(Collections.singletonList(roleUser)));

        User savedEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedEntity);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto newUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        // меняет ли юзер сам себя
        checkUserAccess(existingUser);

        if (!existingUser.getName().equals(newUserDto.getName())
                && userRepository.existsByName(newUserDto.getName())) {
            throw new RuntimeException("Имя пользователя '" + newUserDto.getName() + "' уже занято");
        }

        existingUser.setName(newUserDto.getName());

        if (newUserDto.getPassword() != null && !newUserDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        // удаляет ли юзер сам себя
        checkUserAccess(user);

        userRepository.delete(user);
    }

    // метод проверки прав
    private void checkUserAccess(User targetUser) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!targetUser.getName().equals(currentUsername)) {
            throw new AccessDeniedException("Вы можете управлять только своим аккаунтом");
        }
    }
}