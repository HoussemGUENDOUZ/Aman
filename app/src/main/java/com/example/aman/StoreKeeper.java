package com.example.aman;

public class StoreKeeper {

    String location;
    String storeName;
    public StoreKeeper(){

    }
    public StoreKeeper(String location, String storeName) {
        this.location = location;
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
