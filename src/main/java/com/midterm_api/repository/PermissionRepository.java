package com.midterm_api.repository;

import com.midterm_api.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
