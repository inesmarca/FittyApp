package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.RatingsApiService;
import com.example.fitty.api.UserApiService;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Rating;
import com.example.fitty.dbRoom.DB;

import java.util.List;

public class RatingsRepository {
    private  RatingsApiService apiService;
    private DB database;
    private AppExecutors executors;
    public RatingsRepository(AppExecutors executors, RatingsApiService service, DB database) {
        this.executors = executors;
        this.apiService = service;
        this.database = database;
    }
    public LiveData<Resource<Rating>> rate (int routineId, Rating rating){
        return new NetworkBoundResource<Rating,Void,Rating>(executors,v->null,v->null,v->v)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Rating model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Rating>> createCall() {
                return apiService.rateRoutine(routineId,rating);
            }
        }.asLiveData();
    }
    public LiveData<Resource<List<Rating>>> getRoutineRatings (int routineId, int page,int size,String orderBy,String direction){
        return new NetworkBoundResource<List<Rating>,Void,PagedList<Rating>>(executors,null,null, PagedList::getResults)
        {

            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Rating> model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Rating>>> createCall() {
                return apiService.getRoutineRatings(routineId, page, size, orderBy, direction);
            }
        }.asLiveData();
    }
}
