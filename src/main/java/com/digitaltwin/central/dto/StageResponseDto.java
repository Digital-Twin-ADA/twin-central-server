package com.digitaltwin.central.dto;

public class StageResponseDto {

    private Long id;
    private String name;
    private int capacity;
    private int currentCrowd;
    private boolean overcrowded;
    private String zoneCode;
    private Double latitude;
    private Double longitude;

    public StageResponseDto(Long id, String name, int capacity, int currentCrowd, boolean overcrowded, String zoneCode)
    {
        this(id, name, capacity, currentCrowd, overcrowded, zoneCode, null, null);
    }

    public StageResponseDto(Long id, String name, int capacity, int currentCrowd, boolean overcrowded, String zoneCode,
                            Double latitude, Double longitude)
    {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.currentCrowd = currentCrowd;
        this.overcrowded = overcrowded;
        this.zoneCode = zoneCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentCrowd() {
        return currentCrowd;
    }

    public boolean isOvercrowded() {
        return overcrowded;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    
}
