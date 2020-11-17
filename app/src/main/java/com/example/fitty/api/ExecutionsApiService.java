package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.PagedList;
import com.example.fitty.models.RoutineExecution;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExecutionsApiService {
    @POST("routines/{routineId}/executions")
    LiveData<ApiResponse<RoutineExecution>> execute (@Path("routineId")int routineId, @Body RoutineExecution routineExecution);

    @GET("routines/{routineId}/executions")
    LiveData<ApiResponse<PagedList<RoutineExecution>>> getRoutineExecutions(@Path("routineId")int routineId, @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);
}
