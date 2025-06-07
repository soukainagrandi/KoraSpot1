package com.koraspot.model;

public class Sport {
    private int sportId;
    private String name;

    public Sport() {}

    public Sport(int sportId, String name) {
        this.sportId = sportId;
        this.name = name;
    }

    // Getters et setters
    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
