package com.example.admin.ssuifinalproject.Database.Models;

import com.example.admin.ssuifinalproject.BeatData;

import java.util.Date;

public class PracticeRun {
    private int id;
    private int song_id;
    private double targetBPM;
    private double medianBPM;
    private Date createdAt;

    private BeatData beatData;

    public PracticeRun(int id, int song_id, double targetBPM, double medianBPM, int createdAt, BeatData beatData) {
        this.id = id;
        this.song_id = song_id;
        this.targetBPM = targetBPM;
        this.medianBPM = medianBPM;
        this.createdAt = new Date(createdAt); // convert the time in millis to date object

        this.beatData = beatData;
    }

    //// Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public double getTargetBPM() {
        return targetBPM;
    }

    public void setTargetBPM(double targetBPM) {
        this.targetBPM = targetBPM;
    }

    public double getMedianBPM() {
        return medianBPM;
    }

    public void setMedianBPM(double medianBPM) {
        this.medianBPM = medianBPM;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BeatData getBeatData() {
        return beatData;
    }

    public void setBeatData(BeatData beatData) {
        this.beatData = beatData;
    }
}
