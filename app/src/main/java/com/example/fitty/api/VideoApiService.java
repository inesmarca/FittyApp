package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.PagedList;
import com.example.fitty.models.Video;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VideoApiService {
    @GET("routines/{routineId}/cycles/{cycleId}/exercises/{exerciseId}/videos")
    LiveData<ApiResponse<PagedList<Video>>> getRoutineCycleExerciseVideos(@Path("routineId")int routineId, @Path("cycleId")int cycleId, @Path("exerciseId")int exerciseId, @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);


    @GET("routines/{routineId}/cycles/{cycleId}/exercises/{exerciseId}/videos/{videoId}")
    LiveData<ApiResponse<Video>> getRoutineCycleExerciseVideo(@Path("routineId")int routineId,@Path("cycleId")int cycleId,@Path("exerciseId")int exerciseId,@Path("videoId")int videoId);

}
