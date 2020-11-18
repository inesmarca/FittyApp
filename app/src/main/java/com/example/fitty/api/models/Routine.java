package com.example.fitty.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Routine implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    private int category_id;


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

    private int creator_id;


    @SerializedName("creator")
    @Expose
    private User creator;

    @SerializedName("averageRating")
    @Expose
    private float averageRating;


    private List<Cycle> cycles;

    public Routine(Integer id, String name, String difficulty, Integer id1, Integer creatorId) {
    }

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
     * @param fuerza_de_brazos
     * @param name
     * @param b
     * @param rookie
     * @param category
     */

/*
    public Routine(int id, String name, String difficulty, int category, int creator) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.category_id = category;
        this.creator_id = creator;
    }*/

    public float getAverageRating() {
        return averageRating;
    }

    public int getCategory_id() {
        return category_id;
    }

    public Routine(String name, String detail, String difficulty, Category category, int creator) {
        super();
        this.name = name;
        this.difficulty = difficulty;
        this.category = category;

        String[] strings = detail.split("\\|", 2);
        if(strings.length>=2) {
            this.detail = strings[1];
            this.duration = strings[0] + "'";
        }
        else
            this.detail=strings[0];
        this.creator_id=creator;
    }
    public Routine(String name, String detail, String difficulty, int category, int creator) {
        super();
        this.name = name;
        this.difficulty = difficulty;
        this.category_id = category;

        String[] strings = detail.split("\\|", 2);
        if(strings.length>=2) {
            this.detail = strings[1];
            this.duration = strings[0] + "'";
        }
        else
            this.detail=strings[0];
        this.creator_id=creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        String[] strings = detail.split("\\|", 2);
        if(strings.length>=2)
            return strings[1];
        else
            return strings[0];
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

    public float getRating() { return 3; }

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

    public void addCycle(List<Cycle> _cycles) {
        cycles.addAll(_cycles);
    }


}