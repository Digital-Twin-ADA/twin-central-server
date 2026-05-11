package com.digitaltwin.central.repository;

import com.digitaltwin.central.model.NotificationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationAttemptRepository extends JpaRepository<NotificationAttempt, Long> {
    @Modifying
    void deleteBySubscriberId(Long subscriberId);
}
