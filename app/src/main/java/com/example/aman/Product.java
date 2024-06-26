package com.example.aman;

public class Product {
    public String name;
    String id;
    String description;
    String details;
    String image;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String phoneNumber;

    public String getStoreKeperID() {
        return StoreKeperID;
    }

    public void setStoreKeperID(String storeKeperID) {
        StoreKeperID = storeKeperID;
    }

    String StoreKeperID;

    public Product() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product(String id, String name, String description, String details, String image, String storeKeperID,String phoneNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.details = details;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
