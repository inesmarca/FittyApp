package com.example.fitty;

import android.app.Application;

import com.example.fitty.repository.CategoryRepository;
import com.example.fitty.repository.UserRepository;

public class FittyApp extends Application {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        userRepository = new UserRepository(this);
        categoryRepository=new CategoryRepository(this);

    }
}