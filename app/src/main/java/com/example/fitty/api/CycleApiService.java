package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.api.models.Cycle;
import com.example.fitty.api.models.PagedList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CycleApiService {
    @GET("routines/{routineId}/cycles")
    LiveData<ApiResponse<PagedList<Cycle>>> getRoutineCycles (@Path("routineId") int routineId,@Query("page")int page, @Query("size")int size,@Query("orderBy")String orderBy,@Query("direction")String direction);

    @GET("routines/{routineId}/cycles/{cycleId}")
    LiveData<ApiResponse<Cycle>> getRoutineCycle(@Path("routineId") int routineId, int cycleId);
}
