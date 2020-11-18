package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.UserApiService;
import com.example.fitty.api.VideoApiService;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Video;
import com.example.fitty.dbRoom.DB;

public class VideosRepository {
    private  AppExecutors executors;
    private  VideoApiService service;
    private  DB database;
    public VideosRepository(AppExecutors executors, VideoApiService service, DB database) {
        this.executors = executors;
        this.service = service;
        this.database = database;
    }

    public LiveData<Resource<Video>> getRoutineCycleExerciseVideo (int routineId, int cycleId, int exerciseId,int videoId){
        return new NetworkBoundResource<Video,Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Video model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }


            @NonNull
            @Override
            protected LiveData<ApiResponse<Video>> createCall() {
                return service.getRoutineCycleExerciseVideo(routineId,cycleId,exerciseId,videoId);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<Video>>> getRoutineCycleExerciseVideos (int routineId,int cycleId, int exerciseId , int page, int size, String orderBy, String direction){
        return new NetworkBoundResource<PagedList<Video>,Void>(executors,null,null){

            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Video> model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Video>>> createCall() {
                return service.getRoutineCycleExerciseVideos(routineId,cycleId,exerciseId,page,size,orderBy,direction);
            }
        }


        .asLiveData();
    }
}