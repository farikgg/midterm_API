package com.midterm_api.mapper;

import com.midterm_api.dto.UserDto;
import com.midterm_api.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User userEntity);
    User toEntity(UserDto userDto);
    List<UserDto> toDtoList(List<User> userEntityList);
}
