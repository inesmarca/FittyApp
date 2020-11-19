package com.example.fitty.api;


import android.media.Rating;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoutineApiService {

    @GET("routines")
    LiveData<ApiResponse<PagedList<Routine>>>getRoutines(@Query("search") String seaerch, @Query("difficulty")String difficulty,
           @Query("page") int page, @Query("size") int size,@Query("orderBy") String orderBy,@Query("direction") String direction );

    @GET("routines/{routineId}")
    LiveData<ApiResponse<Routine>>getRoutineById(@Path("routineId") int routineId);


    @GET("routines/{routineId}/ratings")
    LiveData<ApiResponse<Rating>> rateRoutine(@Path("routineId")int routineId);


}
