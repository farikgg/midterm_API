package com.midterm_api.repository;

import com.midterm_api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
