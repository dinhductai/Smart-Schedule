package com.example.smart_schedule.repository;
import com.example.smart_schedule.entity.InvalidatedToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {
    Boolean existsByToken(String token);
}
