package com.example.thuchanhbuoi1.model;

import com.google.gson.annotations.SerializedName;

public class Model {
    private  String name;
    @SerializedName("image")
    private  String image;

    private String realname;

    private String team;

    private String firstappearance;

    private String createdby;

    private String publisher;

    private String bio;

    public Model(String name, String image, String realname, String team, String firstappearance, String createdby, String publisher, String bio) {
        this.name = name;
        this.image = image;
        this.realname = realname;
        this.team = team;
        this.firstappearance = firstappearance;
        this.createdby = createdby;
        this.publisher = publisher;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getRealname() {
        return realname;
    }

    public String getTeam() {
        return team;
    }

    public String getFirstappearance() {
        return firstappearance;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getBio() {
        return bio;
    }
}
