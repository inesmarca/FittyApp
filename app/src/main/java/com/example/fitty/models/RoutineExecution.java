package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class RoutineExecution {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private Timestamp date;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("wasModified")
    @Expose
    private boolean wasModified;
    @SerializedName("routine")
    @Expose
    private Routine routine;


    public RoutineExecution() {
    }


    public RoutineExecution(int id, Timestamp date, int duration, boolean wasModified, Routine routine) {
        super();
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.wasModified = wasModified;
        this.routine = routine;
    }

    public RoutineExecution(int duration, boolean wasModified) {
        this.duration = duration;
        this.wasModified = wasModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isWasModified() {
        return wasModified;
    }

    public void setWasModified(boolean wasModified) {
        this.wasModified = wasModified;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

}