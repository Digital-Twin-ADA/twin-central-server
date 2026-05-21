package com.digitaltwin.central.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StageRequestDto {

    @NotBlank(message = "name is required")
    private String name;
    @Min(value = 0, message = "capacity must be >= 0")
    private int capacity;
    private String zoneCode;
    private Double latitude;
    private Double longitude;

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getZoneCode()
    {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode)
    {
        this.zoneCode = zoneCode;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
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

    
}
