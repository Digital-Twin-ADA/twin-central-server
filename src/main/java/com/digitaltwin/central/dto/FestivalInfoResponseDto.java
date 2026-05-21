package com.digitaltwin.central.dto;

import java.util.List;

public class FestivalInfoResponseDto {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private List<StageResponseDto> stages;

    public FestivalInfoResponseDto(Long id, String name, Double latitude, Double longitude, String description,
                                   List<StageResponseDto> stages) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.stages = stages;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public List<StageResponseDto> getStages() {
        return stages;
    }
}
