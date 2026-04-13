package com.digitaltwin.central.dto;

public class TelemetryRequest {
    private Long stageId;
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
