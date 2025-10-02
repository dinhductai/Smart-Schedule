package com.example.smart_schedule.repository;
import com.example.smart_schedule.entity.PushSubscription;
import com.example.smart_schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    // Tìm tất cả các subscription của một người dùng (trên nhiều thiết bị)
    List<PushSubscription> findAllByUser(User user);
}