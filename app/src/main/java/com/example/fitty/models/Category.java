package com.example.fitty.models;

import com.example.fitty.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("detail")
    @Expose
    private String detail;



    public void setName(String name) {
        this.name = name;
    }


    public void setDetail(String detail) {
        this.detail = detail;
    }




    /**
     * No args constructor for use in serialization
     *
     */
    public Category() {
    }

    /**
     *
     * @param id
     */
    public Category(int id, String name, String detail) {
        super();
        this.id = id;
        this.name = name;
        this.detail = detail;
    }

    public int getIcon() {
        switch (name) {
            case "fuerza":
                return R.drawable.ic_strength;
            case "tren superior":
                return R.drawable.ic_abs;
            case "elongacion":
                return R.drawable.ic_movilidad;
            case "resistencia":
                return R.drawable.ic_resitencia;
            case "piernas":
                return R.drawable.ic_legs;
            case "relajacion":
                return R.drawable.ic_tulip;
            case "yoga":
                return R.drawable.ic_yoga;
            default:
                return R.drawable.ic_cardio;
        }
    }

    public String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public int getId() {
        return id;
    }

    public String getName() { return toTitleCase(name); }

    public String getDetail() { return detail; }

    public void setId(int id) {
        this.id = id;
    }

}