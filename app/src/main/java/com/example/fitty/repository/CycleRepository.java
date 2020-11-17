package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.CycleApiService;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.PagedList;

public class CycleRepository {
    private final CycleApiService apiService;

    public CycleRepository(Context context){
        this.apiService = ApiClient.create(context,CycleApiService.class);
    }

    public LiveData<Resource<PagedList<Cycle>>> getRoutineCycles (int routineId,int page,int size,String orderBy,String direction){
        return new NetworkBoundResource<PagedList<Cycle>, PagedList<Cycle>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Cycle>>> createCall() {
                return apiService.getRoutineCycles(routineId,page,size,orderBy,direction);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Cycle>> getRoutineCycle(int routineId,int cycleId){
        return new NetworkBoundResource<Cycle,Cycle>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Cycle>> createCall() {
                return apiService.getRoutineCycle(routineId,cycleId);
            }
        }.asLiveData();
    }
}
