package com.example.smart_schedule.repository;
import com.example.smart_schedule.entity.Task;
import com.example.smart_schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Tìm các task có deadline nằm trong một khoảng thời gian nhất định (để gửi nhắc nhở)
    List<Task> findAllByDeadlineBetween(LocalDateTime start, LocalDateTime end);
}