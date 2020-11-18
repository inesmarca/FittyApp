package com.example.fitty;

import android.app.Application;

import androidx.room.Room;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.CategoryApiService;
import com.example.fitty.api.CycleApiService;
import com.example.fitty.api.ExerciseApiService;
import com.example.fitty.api.RatingsApiService;
import com.example.fitty.api.RoutineApiService;
import com.example.fitty.api.UserApiService;
import com.example.fitty.api.VideoApiService;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.repository.AppExecutors;
import com.example.fitty.repository.CategoryRepository;
import com.example.fitty.repository.CycleRepository;
import com.example.fitty.repository.ExerciseRepository;
import com.example.fitty.repository.RatingsRepository;
import com.example.fitty.repository.RoutineRepository;
import com.example.fitty.repository.UserRepository;
import com.example.fitty.repository.VideosRepository;

public class FittyApp extends Application {

    private UserRepository userRepository;
    AppExecutors appExecutors;
    AppPreferences preferences;
    private CycleRepository cycleRepository;
    private RatingsRepository ratingsRepository;
    private ExerciseRepository exerciseRepository;

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public AppPreferences getPreferences() {
        return preferences;
    }

    private VideosRepository videosRepository;

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

        preferences = new AppPreferences(this);

        appExecutors = new AppExecutors();

        DB database = Room.databaseBuilder(this, DB.class, "FITTY_DB").build();

        UserApiService userService = ApiClient.create(this, UserApiService.class);

        RoutineApiService routineApiService = ApiClient.create(this,RoutineApiService.class);
        CategoryApiService categoryApiService = ApiClient.create(this,CategoryApiService.class);
        VideoApiService videoApiService = ApiClient.create(this,VideoApiService.class);

        RatingsApiService ratingsApiService = ApiClient.create(this,RatingsApiService.class);

        CycleApiService cycleApiService = ApiClient.create(this,CycleApiService.class);

        ExerciseApiService exerciseApiService = ApiClient.create(this,ExerciseApiService.class);

        userRepository = new UserRepository(appExecutors,userService,database);
        categoryRepository=new CategoryRepository(appExecutors,categoryApiService,database);
        routineRepository = new RoutineRepository(appExecutors,routineApiService,database);
        videosRepository = new VideosRepository(appExecutors,videoApiService,database);
        exerciseRepository = new ExerciseRepository(appExecutors,exerciseApiService,database);
        cycleRepository = new CycleRepository(appExecutors,cycleApiService,database);
        ratingsRepository = new RatingsRepository(appExecutors,ratingsApiService,database);
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

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

}
