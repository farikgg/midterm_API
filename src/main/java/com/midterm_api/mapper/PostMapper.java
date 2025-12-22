package com.midterm_api.mapper;

import com.midterm_api.models.Post;
import com.midterm_api.dto.PostDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto(Post postEntity);
    Post toEntity(PostDto postDto);
    List<PostDto> toDtoList(List<Post> postsEntityList);
}
