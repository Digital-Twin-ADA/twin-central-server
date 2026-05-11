package com.digitaltwin.central.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notification_attempts")
public class NotificationAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "subscriber_id")
    private Long subscriberId;

    @Column(name = "status")
    private String status;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "last_response", columnDefinition = "TEXT")
    private String lastResponse;

    @Column(name = "last_attempt_at")
    private OffsetDateTime lastAttemptAt;

    public NotificationAttempt() {
    }

    public Long getId() {
        return id;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public OffsetDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(OffsetDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }
}
