package com.example.irfan.livelocationtracker;

public class Tracking {
    private String email,uid;
    private Double lat,lng;

    public Tracking() {
    }

    public Tracking(String email, String uid, Double lat, Double lng) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
