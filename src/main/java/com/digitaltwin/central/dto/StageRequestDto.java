package com.digitaltwin.central.dto;

public class StageRequestDto {

    private String name;
    private int capacity;
    private String zoneCode;

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


    
}
