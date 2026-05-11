package com.digitaltwin.central.repository;

import com.digitaltwin.central.model.WebhookSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookSubscriberRepository extends JpaRepository<WebhookSubscriber, Long> {
    List<WebhookSubscriber> findByActiveTrue();
}
