package com.example.smart_schedule.repository;

import com.example.smart_schedule.entity.Role;
import com.example.smart_schedule.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Tự động tạo câu lệnh query để tìm Role dựa trên roleName
    Optional<Role> findByRoleName(RoleName roleName);
}