package com.digitaltwin.central.dto;

import jakarta.validation.constraints.NotBlank;

public class FestivalInfoRequestDto {

    @NotBlank(message = "name is required")
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
