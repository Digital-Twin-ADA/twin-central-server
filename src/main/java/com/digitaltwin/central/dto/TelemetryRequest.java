package com.digitaltwin.central.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TelemetryRequest {
    @NotNull(message = "stageId is required")
    private Long stageId;

    @Min(value = 0, message = "currentCrowd must be >= 0")
    private int currentCrowd;

    public Long getStageId()
    {
        return stageId;
    }

    public int getCurrentCrowd()
    {
        return currentCrowd;
    }

    public void setStageId(Long stageId)
    {
        this.stageId = stageId;
    }

    public void setCurrentCrowd(int currentCrowd)
    {
        this.currentCrowd = currentCrowd;
    }
}
