package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Rating;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RatingsApiService {
    @POST("routines/{routineId}/ratings")
    LiveData<ApiResponse<Rating>> rateRoutine (@Path("routineId")int routineId, @Body Rating rating);

    @GET("routines/{routineId}/ratings")
    LiveData<ApiResponse<PagedList<Rating>>> getRoutineRatings (@Path("routineId") int routineId, @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);



}
