package com.example.myapplication;

public class NasaItem {
    private String title;
    private String date;
    private String copyright;
    private String imgUrl;
    private String base64Image;

    public NasaItem(String title, String date, String copyright, String imgUrl, String base64Image) {
        this.title = title;
        this.date = date;
        this.copyright = copyright;
        this.imgUrl = imgUrl;
        this.base64Image = base64Image;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getBase64Image() {
        return base64Image;
    }
}
