package com.example.admin.ssuifinalproject.Database.Models;

import java.util.Date;

public class Song {

    private int id;
    private String title;
    private double targetBPM;
    private Date createdAt;

    public Song(int id, String title, double targetBPM, int createdAt) {
        this.id = id;
        this.title = title;
        this.targetBPM = targetBPM;
        this.createdAt = new Date(createdAt); // convert the time in millis to date object
    }

    //// Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTargetBPM() {
        return targetBPM;
    }

    public void setTargetBPM(double targetBPM) {
        this.targetBPM = targetBPM;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
