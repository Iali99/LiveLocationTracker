package com.example.irfan.livelocationtracker;

public class IncidentClass {
    private String incidentType;
    private int incidentIntensity;
    private double incidentLat, incidentLon;

    public IncidentClass() {
    }

    public IncidentClass(String incidentType, int incidentIntensity, double incidentLat, double incidentLon) {
        this.incidentType = incidentType;
        this.incidentIntensity = incidentIntensity;
        this.incidentLat = incidentLat;
        this.incidentLon = incidentLon;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public int getIncidentIntensity() {
        return incidentIntensity;
    }

    public void setIncidentIntensity(int incidentIntensity) {
        this.incidentIntensity = incidentIntensity;
    }

    public double getIncidentLat() {
        return incidentLat;
    }

    public void setIncidentLat(double incidentLat) {
        this.incidentLat = incidentLat;
    }

    public double getIncidentLon() {
        return incidentLon;
    }

    public void setIncidentLon(double incidentLon) {
        this.incidentLon = incidentLon;
    }
}
