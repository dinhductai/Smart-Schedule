package com.example.smart_schedule.repository;

import com.example.smart_schedule.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // Hiện tại chưa cần phương thức truy vấn tùy chỉnh
}
