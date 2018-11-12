package com.example.irfan.livelocationtracker;

public class Tracking {
    private String email,uid;
    private Double lat,lng;
    private int counter;
    private boolean alert;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public Tracking() {
    }

    public Tracking(String email, String uid, Double lat, Double lng, int counter, boolean alert) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.counter = counter;
        this.alert = alert;
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
