package com.example.fitty.api;


import android.media.Rating;

import androidx.lifecycle.LiveData;

import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Routine;
import com.example.fitty.api.models.RoutineExecution;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoutineApiService {

    @GET("routines")
    LiveData<ApiResponse<PagedList<Routine>>>getRoutines(@Query("search") String seaerch, @Query("difficulty")String difficulty,
           @Query("page") int page, @Query("size") int size,@Query("orderBy") String orderBy,@Query("direction") String direction );


    @GET("routines/{routineId}/ratings")
    LiveData<ApiResponse<Rating>> rateRoutine(@Path("routineId")int routineId);

    @POST("routines/{routineId}/executions")
    LiveData<ApiResponse<RoutineExecution>> execute (@Path("routineId")int routineId, @Body RoutineExecution routineExecution);

    @GET("routines/{routineId}/executions")
    LiveData<ApiResponse<PagedList<RoutineExecution>>> getRoutineExecutions(@Path("routineId")int routineId, @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);

    @GET("routines/{routineId}")
    LiveData<ApiResponse<Routine>> getRoutine(@Path("routineId")int routineId);

}
