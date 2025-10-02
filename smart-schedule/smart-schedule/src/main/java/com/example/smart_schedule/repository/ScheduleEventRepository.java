package com.example.smart_schedule.repository;
import com.example.smart_schedule.entity.ScheduleEvent;
import com.example.smart_schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleEventRepository extends JpaRepository<ScheduleEvent, Long> {

    // Tìm tất cả các sự kiện trên lịch của một người dùng
    List<ScheduleEvent> findAllByUser(User user);
}