package com.example.admin.ssuifinalproject.Database.Models;

import java.util.Date;

public class BPM {

    private int id;
    private int practiceRun_id;
    private double time;
    private Date createdAt;

    // constructor without ID or time
    public BPM(int practiceRun_id, double time) {
        this.practiceRun_id = practiceRun_id;
        this.time = time;
    }

    // constructor without ID
    public BPM(int practiceRun_id, double time, int createdAt) {
        this(practiceRun_id, time);
        this.createdAt = new Date(createdAt); // convert the time in millis to date object
    }

    // constructor with ID
    public BPM(int id, int practiceRun_id, double time, int createdAt) {
        this(practiceRun_id, time, createdAt);
        this.id = id;
    }

    //// Getters and setters
    public int getId() {
        return id;
    }

    public int getPracticeRun_id() {
        return practiceRun_id;
    }

    public double getTime() {
        return time;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPracticeRun_id(int practiceRun_id) {
        this.practiceRun_id = practiceRun_id;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

