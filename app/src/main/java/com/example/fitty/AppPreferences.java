package com.example.fitty;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private final String AUTH_TOKEN = "auth_token";
    private final String EMAIL ="email";
    private final String USERNAME ="username";

    private SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.apply();
    }

    public void setUsername(String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME,username);
        editor.apply();
    }

    public String getUsername(){
        return sharedPreferences.getString(USERNAME,null);
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL,null);
    }
}