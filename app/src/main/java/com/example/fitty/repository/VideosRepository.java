package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.RatingsApiService;
import com.example.fitty.api.VideoApiService;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Rating;
import com.example.fitty.models.Video;

public class VideosRepository {
    private final VideoApiService apiService;

    public VideosRepository(Context context){
        apiService = ApiClient.create(context,VideoApiService.class);
    }

    public LiveData<Resource<Video>> getRoutineCycleExerciseVideo (int routineId, int cycleId, int exerciseId,int videoId){
        return new NetworkBoundResource<Video,Video>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Video>> createCall() {
                return apiService.getRoutineCycleExerciseVideo(routineId,cycleId,exerciseId,videoId);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<Video>>> getRoutineRatings (int routineId,int cycleId, int exerciseId , int page, int size, String orderBy, String direction){
        return new NetworkBoundResource<PagedList<Video>,PagedList<Video>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Video>>> createCall() {
                return apiService.getRoutineCycleExerciseVideos(routineId,cycleId,exerciseId,page,size,orderBy,direction);
            }
        }.asLiveData();
    }
}