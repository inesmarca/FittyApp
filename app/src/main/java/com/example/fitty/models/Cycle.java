package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cycle {
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
    @SerializedName("order")
    @Expose
    private int order;
    @SerializedName("repetitions")
    @Expose
    private int repetitions;

    private List<Exercise> exercises = new ArrayList<>();

    public Cycle(int id, String name, String detail, String type, int order, int repetitions) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.order = order;
        this.repetitions = repetitions;
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

    public int getOrder() {
        return order;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void addExercise(List<Exercise> listExercises) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.addAll(listExercises);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cycle cycle = (Cycle) o;
        return id == cycle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
