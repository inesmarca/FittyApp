package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Routine {

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

    /**
     * No args constructor for use in serialization
     *
     */
    public Routine() {
    }

    /**
     *
     * @param difficulty
     * @param name
     * @param isPublic
     * @param detail
     * @param category
     */
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
        return detail;
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

    public String getDuration() { return duration; }

}