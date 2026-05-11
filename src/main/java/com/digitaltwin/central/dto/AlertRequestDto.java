package com.digitaltwin.central.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlertRequestDto {
    private Long stageId;

    @NotBlank(message = "type is required")
    @Size(max = 100)
    private String type;

    @NotBlank(message = "message is required")
    private String message;

    @NotBlank(message = "severity is required")
    private String severity;

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
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
}
