package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("title")
    private String title;

    @SerializedName("date")
    private String date;

    @SerializedName("base64Image")
    private String base64Image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
// Các getter và setter cho các thuộc tính
}
