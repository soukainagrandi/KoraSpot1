package com.koraspot.model;

public class Field {
    private int fieldId;
    private int locationId;
    private int sportId;
    private String name;
    private String description;
    private double pricePerHour;

    public Field() {}

    public Field(int fieldId, int locationId, int sportId, String name, String description, double pricePerHour) {
        this.fieldId = fieldId;
        this.locationId = locationId;
        this.sportId = sportId;
        this.name = name;
        this.description = description;
        this.pricePerHour = pricePerHour;
    }

    // Getters et setters
    public int getFieldId() { return fieldId; }
    public void setFieldId(int fieldId) { this.fieldId = fieldId; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
}
