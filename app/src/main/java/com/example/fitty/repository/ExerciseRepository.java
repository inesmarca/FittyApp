package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.ExerciseApiService;
import com.example.fitty.api.models.Exercise;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.dbRoom.DB;

import java.util.concurrent.TimeUnit;

public class ExerciseRepository {
    private AppExecutors executors;
    private ExerciseApiService service;
    private DB database;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    public ExerciseRepository(AppExecutors executors,ExerciseApiService service, DB database) {
        this.service=service;
        this.executors=executors;
        this.database=database;
    }

    public LiveData<Resource<Exercise>> getRoutineCycleExercise(int routineId, int cycleId, int exerciseId) {
        return new NetworkBoundResource<Exercise, Void>(executors,null,null) {

            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Exercise model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Exercise>> createCall() {
                return service.getRoutineCycleExercise(routineId, cycleId, exerciseId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<Exercise>>> getRoutineCycleExercises(int routineId, int cycleId, int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Exercise>,Void>(executors,null,null) {


            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Exercise> model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Exercise>>> createCall() {
                return service.getRoutineCycleExercises(routineId, cycleId, page, size, orderBy, direction);
            }
        }.asLiveData();
    }
}
