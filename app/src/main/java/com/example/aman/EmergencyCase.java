package com.example.aman;

import java.util.Date;

public class EmergencyCase {
    long time;
    double latitude,longitude;
    String type,description,image,user_id,emergency_unit_id;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmergency_unit_id() {
        return emergency_unit_id;
    }

    public void setEmergency_unit_id(String emergency_unit_id) {
        this.emergency_unit_id = emergency_unit_id;
    }
    public EmergencyCase() {

    }
    public EmergencyCase(double latitude, double longitude, long time, String type, String description, String image, String user_id, String emergency_unit_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.type = type;
        this.description = description;
        this.image = image;
        this.user_id = user_id;
        this.emergency_unit_id = emergency_unit_id;
    }
}
