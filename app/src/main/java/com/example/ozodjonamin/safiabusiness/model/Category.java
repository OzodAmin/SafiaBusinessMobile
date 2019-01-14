package com.example.ozodjonamin.safiabusiness.model;

import com.squareup.moshi.Json;

public class Category {
    @Json(name = "id")
    private String id;
    @Json(name = "title")
    private String title;
    @Json(name = "image")
    private String image;

    public Category() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
