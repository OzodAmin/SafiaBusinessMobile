package com.example.ozodjonamin.safiabusiness.model;

import com.squareup.moshi.Json;

public class Order {

    @Json(name = "id")
    private String id;
    @Json(name = "status")
    private int status;
    @Json(name = "created_at")
    private String created_at;

    public Order() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
