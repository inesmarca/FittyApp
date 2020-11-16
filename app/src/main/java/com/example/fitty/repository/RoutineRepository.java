package com.example.fitty.repository;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.RoutineApiService;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;

public class RoutineRepository {
    private final RoutineApiService apiService;

    public RoutineRepository(Context context){
        this.apiService = ApiClient.create(context,RoutineApiService.class);
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines(String search,String difficulty, int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiService.getRoutines(search,difficulty,page,size,orderBy,direction);
            }
        }.asLiveData();
    }
}
