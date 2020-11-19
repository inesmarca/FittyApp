package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Routine implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("isPublic")
    @Expose
    private boolean isPublic;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("creator")
    @Expose
    private User creator;

    private List<Cycle> cycles = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Routine() {
    }


    public Routine(String name, String detail, boolean isPublic, String difficulty, Category category) {
        super();
        this.name = name;
        this.isPublic = isPublic;
        this.difficulty = difficulty;
        this.category = category;

        String[] strings = detail.split("\\|", 2);
        this.detail = strings[1];
        this.duration = strings[0] + "'";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        String[] strings = detail.split("\\|", 2);
        return strings[1];
    }

    public int getId() {
        return id;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getRating() { return 3; }

    public String getDuration() {
        String[] strings = detail.split("\\|", 2);
        return strings[0];
    }

    public Boolean isFavorite() {
        return true;
    }

    public void addCycle(Cycle cycle) {
        cycles.add(cycle);
    }

    public void addCycle(List<Cycle> listCycles) {
        cycles.addAll(listCycles);
    }

    public List<Cycle> getCycles() { return this.cycles; }

}