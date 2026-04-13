package com.digitaltwin.central.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stages")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private int capacity;

    private int currentCrowd;

    private boolean overcrowded;

    private String zoneCode;

    // Constructors
    public Stage() {
    }

    public Stage(String name, int capacity, int currentCrowd, boolean overcrowded, String zoneCode) {
        this.name = name;
        this.capacity = capacity;
        this.currentCrowd = currentCrowd;
        this.overcrowded = overcrowded;
        this.zoneCode = zoneCode;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentCrowd() {
        return currentCrowd;
    }

    public void setCurrentCrowd(int currentCrowd) {
        this.currentCrowd = currentCrowd;
    }

    public boolean isOvercrowded() {
        return overcrowded;
    }

    public void setOvercrowded(boolean overcrowded) {
        this.overcrowded = overcrowded;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
}