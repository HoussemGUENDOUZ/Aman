package com.example.aman;

import java.util.Date;

public class EmergencyCase {

    String id ;
    String position;
    Date time;
    String type;
    String description;
    String image;
    String userId;
    String emergencyServiceUnitId;

    public EmergencyCase(String id, String position, Date time, String type, String description, String image, String userId, String emergencyServiceUnitId) {
        this.id = id;
        this.position = position;
        this.time = time;
        this.type = type;
        this.description = description;
        this.image = image;
        this.userId = userId;
        this.emergencyServiceUnitId = emergencyServiceUnitId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmergencyServiceUnitId() {
        return emergencyServiceUnitId;
    }

    public void setEmergencyServiceUnitId(String emergencyServiceUnitId) {
        this.emergencyServiceUnitId = emergencyServiceUnitId;
    }


}
