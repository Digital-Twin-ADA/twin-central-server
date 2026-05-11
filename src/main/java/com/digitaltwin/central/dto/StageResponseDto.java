package com.digitaltwin.central.dto;

public class StageResponseDto {

    private Long id;
    private String name;
    private int capacity;
    private int currentCrowd;
    private boolean overcrowded;
    private String zoneCode;

    public StageResponseDto(Long id, String name, int capacity, int currentCrowd, boolean overcrowded, String zoneCode)
    {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.currentCrowd = currentCrowd;
        this.overcrowded = overcrowded;
        this.zoneCode = zoneCode;
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
    
}
