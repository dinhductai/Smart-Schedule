package com.example.smart_schedule.enumeration;

import com.example.smart_schedule.entity.Task;
import com.example.smart_schedule.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedule_events")
public class ScheduleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // Quan hệ nhiều-một: Nhiều sự kiện thuộc về một User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ nhiều-một: Một sự kiện có thể liên quan đến một Task (hoặc không)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_task_id", nullable = true)
    private Task relatedTask;
}