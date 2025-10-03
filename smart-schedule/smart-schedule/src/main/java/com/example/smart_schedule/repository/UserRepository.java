package com.example.smart_schedule.repository;

import com.example.smart_schedule.entity.User;
import com.example.smart_schedule.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<RoleName> findRoleNamesByUserId(Long userId);
    Boolean existsByEmail(String email);

}