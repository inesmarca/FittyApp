package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.Exercise;
import com.example.fitty.models.PagedList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExerciseApiService {
    @GET("routines/{routineId}/cycles/{cycleId}/exercises")
    LiveData<ApiResponse<PagedList<Exercise>>> getRoutineCycleExercises(@Path("routineId")int routineId, @Path("cycleId")int cycleId, @Query("page")int page,@Query("size")int size,@Query("orderBy")String orderBy,@Query("direction")String direction);

    @GET("routines/{routineId}/cycles/{cycleId}/exercises/{exerciseId}")
    LiveData<ApiResponse<Exercise>> getRoutineCycleExercise(@Path("routineId")int routineId, @Path("cycleId")int cycleId,@Path("exerciseId")int exerciseId);
}
