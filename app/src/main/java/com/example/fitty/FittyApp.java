package com.example.fitty;

import android.app.Application;

import com.example.fitty.models.Video;
import com.example.fitty.repository.CategoryRepository;
import com.example.fitty.repository.CycleRepository;
import com.example.fitty.repository.ExerciseRepository;
import com.example.fitty.repository.RatingsRepository;
import com.example.fitty.repository.RoutineRepository;
import com.example.fitty.repository.UserRepository;
import com.example.fitty.repository.VideosRepository;

public class FittyApp extends Application {

    private UserRepository userRepository;
    private CycleRepository cycleRepository;
    private RatingsRepository ratingsRepository;
    private ExerciseRepository exerciseRepository;
    private VideosRepository videosRepository;
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    private CategoryRepository categoryRepository;
    private RoutineRepository routineRepository;
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
        routineRepository = new RoutineRepository(this);
        videosRepository = new VideosRepository(this);
        exerciseRepository = new ExerciseRepository(this);
        cycleRepository = new CycleRepository(this);
        ratingsRepository = new RatingsRepository(this);
    }

    public CycleRepository getCycleRepository() {
        return cycleRepository;
    }

    public RatingsRepository getRatingsRepository() {
        return ratingsRepository;
    }

    public ExerciseRepository getExerciseRepository() {
        return exerciseRepository;
    }

    public VideosRepository getVideosRepository() {
        return videosRepository;
    }
}
