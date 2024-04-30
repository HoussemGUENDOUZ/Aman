package com.example.aman;

import java.util.Date;

public class Order {

    String id;
    String status;
    int rating;
    long created_at;

    public long getCreated_at() {
        return created_at;
    }

    public long getConfirmed_at() {
        return confirmed_at;
    }

    long confirmed_at;
    String service_provider_id;
    String client_id;

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setConfirmed_at(long confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Order() {

    }

    public Order(String id, String status, int rating, long created_at, long confirmed_at, String service_provider_id, String client_id) {
        this.id = id;
        this.status = status;
        this.rating = rating;
        this.created_at = created_at;
        this.confirmed_at = confirmed_at;
        this.service_provider_id = service_provider_id;
        this.client_id = client_id;
    }
}
