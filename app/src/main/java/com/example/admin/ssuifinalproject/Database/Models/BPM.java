package com.example.admin.ssuifinalproject.Database.Models;

import java.util.Date;

public class BPM {

    private int id;
    private int practiceRun_id;
    private double time;
    private Date createdAt;

    public BPM(int id, int practiceRun_id, double time, int createdAt) {
        this.id = id;
        this.practiceRun_id = practiceRun_id;
        this.time = time;
        this.createdAt = new Date(createdAt); // convert the time in millis to date object
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
}

