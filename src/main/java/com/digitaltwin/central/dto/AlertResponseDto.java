package com.digitaltwin.central.dto;

import java.time.OffsetDateTime;

public class AlertResponseDto {
    private Long id;
    private Long stageId;
    private String type;
    private String message;
    private String severity;
    private OffsetDateTime createdAt;
    private boolean resolved;
    private OffsetDateTime resolvedAt;

    public AlertResponseDto(Long id, Long stageId, String type, String message, String severity, OffsetDateTime createdAt, boolean resolved, OffsetDateTime resolvedAt) {
        this.id = id;
        this.stageId = stageId;
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.createdAt = createdAt;
        this.resolved = resolved;
        this.resolvedAt = resolvedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getStageId() {
        return stageId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public OffsetDateTime getResolvedAt() {
        return resolvedAt;
    }
}
