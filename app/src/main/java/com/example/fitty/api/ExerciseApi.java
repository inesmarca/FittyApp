package com.example.fitty.api;


import androidx.lifecycle.LiveData;

import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseApi {

    @GET("routines")
    LiveData<PagedList<Routine>> getRoutines();
}
