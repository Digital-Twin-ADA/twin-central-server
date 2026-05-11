package com.digitaltwin.central.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "webhook_subscribers")
public class WebhookSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    // optional secret used to sign payloads or for identification
    private String secret;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public WebhookSubscriber() {
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
