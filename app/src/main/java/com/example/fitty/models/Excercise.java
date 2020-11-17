package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Excercise {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("repetitions")
    @Expose
    private String repetitions;
    @SerializedName("order")
    @Expose
    private String order;

    public Excercise(int id, String name, String detail, String type, String duration, String repetitions, String order) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.duration = duration;
        this.repetitions = repetitions;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public String getOrder() {
        return order;
    }
}
