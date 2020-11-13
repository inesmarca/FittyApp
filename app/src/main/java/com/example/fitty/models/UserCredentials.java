package com.example.fitty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCredentials {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserCredentials() {
    }

    /**
     *
     * @param password
     * @param username
     */
    public UserCredentials(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}