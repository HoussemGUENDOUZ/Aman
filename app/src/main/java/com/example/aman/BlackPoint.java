package com.example.aman;

import java.util.Date;

public class BlackPoint {

    private String addedAt;
    private String addedBy;
    private double latitude;
    private double longitude;
    private String type;

    public BlackPoint(String addedAt, String addedBy, double latitude, double longitude, String type) {
        this.addedAt = addedAt;
        this.addedBy = addedBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }
    public BlackPoint(){

    }
    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
