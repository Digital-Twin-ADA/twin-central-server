package com.digitaltwin.central.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "severity")
    private String severity;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "resolved")
    private boolean resolved;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    public Alert() {
    }

    public Long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public OffsetDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(OffsetDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
