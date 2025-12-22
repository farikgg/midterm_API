package com.midterm_api.repository;

import com.midterm_api.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);

    List<Post> findAllByUserId(Long userId);
}