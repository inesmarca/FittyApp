package com.example.fitty.api;


import android.media.Rating;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.Category;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RoutineApiService {

    @GET("routines")
    LiveData<PagedList<Routine>> getRoutines();


    @GET("routines/{routineId}/ratings")
    LiveData<ApiResponse<Rating>> rateRoutine(@Path("routineId")int routineId);


}
