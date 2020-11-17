package com.example.fitty.repository;

import android.content.Context;
import android.content.pm.SharedLibraryInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.RatingsApiService;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Rating;

public class RatingsRepository {
    private final RatingsApiService apiService;

    public RatingsRepository(Context context){
        apiService = ApiClient.create(context,RatingsApiService.class);
    }

    public LiveData<Resource<Rating>> rate (int routineId, Rating rating){
        return new NetworkBoundResource<Rating,Rating>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Rating>> createCall() {
                return apiService.rateRoutine(routineId,rating);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<Rating>>> getRoutineRatings (int routineId, int page,int size,String orderBy,String direction){
        return new NetworkBoundResource<PagedList<Rating>,PagedList<Rating>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Rating>>> createCall() {
                return apiService.getRoutineRatings(routineId,page,size,orderBy,direction);
            }
        }.asLiveData();
    }
}
