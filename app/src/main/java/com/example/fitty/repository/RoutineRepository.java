package com.example.fitty.repository;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.ExecutionsApiService;
import com.example.fitty.api.RoutineApiService;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Rating;
import com.example.fitty.models.Routine;
import com.example.fitty.models.RoutineExecution;

public class RoutineRepository {
    private final RoutineApiService apiService;
    private final ExecutionsApiService executionsApiService;
    public RoutineRepository(Context context){
        this.apiService = ApiClient.create(context,RoutineApiService.class);
        this.executionsApiService = ApiClient.create(context,ExecutionsApiService.class);
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
    public LiveData<Resource<RoutineExecution>> executeRoutine (int routineId, RoutineExecution routineExecution){
        return new NetworkBoundResource<RoutineExecution,RoutineExecution>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<RoutineExecution>> createCall() {
                return executionsApiService.execute(routineId,routineExecution);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<RoutineExecution>>> getRoutineExecutions (int routineId, int page, int size, String orderBy, String dir){
        return new NetworkBoundResource<PagedList<RoutineExecution>,PagedList<RoutineExecution>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineExecution>>> createCall() {
                return executionsApiService.getRoutineExecutions(routineId,page,size,orderBy,dir);
            }
        }.asLiveData();
    }

}
