package com.example.aman;

public class ServicesAvailable {

    int service_img;
    String service_type;

    public int getService_img() {
        return service_img;
    }

    public void setService_img(int service_img) {
        this.service_img = service_img;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public ServicesAvailable(int service_img, String service_type) {
        this.service_img = service_img;
        this.service_type = service_type;
    }
}
