package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.ExerciseApiService;
import com.example.fitty.api.RatingsApiService;
import com.example.fitty.models.Exercise;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Rating;

public class ExerciseRepository {
    private final ExerciseApiService apiService;

    public ExerciseRepository(Context context) {
        apiService = ApiClient.create(context, ExerciseApiService.class);
    }

    public LiveData<Resource<Exercise>> getRoutineCycleExercise(int routineId, int cycleId, int exerciseId) {
        return new NetworkBoundResource<Exercise, Exercise>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Exercise>> createCall() {
                return apiService.getRoutineCycleExercise(routineId, cycleId, exerciseId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<Exercise>>> getRoutineCycleExercises(int routineId, int cycleId, int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Exercise>, PagedList<Exercise>>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Exercise>>> createCall() {
                return apiService.getRoutineCycleExercises(routineId, cycleId, page, size, orderBy, direction);
            }
        }.asLiveData();
    }
}
