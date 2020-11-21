package com.example.fitty.repository;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Entity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.fitty.R;
import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.RoutineApiService;
import com.example.fitty.api.UserApiService;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.Error;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Routine;
import com.example.fitty.api.models.RoutineExecution;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.dbRoom.entitys.CategoryEntity;
import com.example.fitty.dbRoom.entitys.RoutineEntity;
import com.example.fitty.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class RoutineRepository {
    private RoutineApiService apiService;
    private AppExecutors executors;
    private DB database;
    private  Category category;
    private  Category _category;
    private CategoryRepository categoryRepository;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    private Semaphore semaphore;
    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";

    public RoutineRepository(AppExecutors executors, RoutineApiService service, DB database, CategoryRepository categoryRepository) {
        this.executors = executors;
        this.apiService = service;
        this.database = database;
        this.categoryRepository  = categoryRepository;
        //semaphore = new Semaphore(0);

    }
    /*
    private Routine entityToDomain (RoutineEntity entity){
        return new Routine(entity.id,entity.name,entity.difficulty, Objects.requireNonNull(database.categoryDao().findById(entity.categoryId).getValue()).id,entity.creatorId);
    }
    private RoutineEntity domainToEntity(Routine routine){
        return new RoutineEntity(routine.getId(),routine.getDetail(),routine.getAverageRating(),routine.getName(),routine.getCreator().getId(),routine.getCategory().getId(),routine.getDifficulty());
    }*/

    public LiveData<Resource<List<Routine>>> getRoutines(String search, String difficulty, int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<Routine>>(executors,
                entities -> entities.stream().map(entity -> new Routine(entity.id, entity.name, entity.detail, entity.difficulty, new Category(entity.category_id, entity.category_name, entity.category_detail), entity.creatorId)).collect(toList()),

                domain ->
                        domain.getResults().stream().map(routine -> new RoutineEntity(routine.getId(), routine.getDetail(), routine.getAverageRating(), routine.getName(), routine.getCreator().getId(), routine.getDifficulty(), routine.getCategory().getId(), routine.getCategory().getName(), routine.getCategory().getDetail()))
                                .collect(toList()),

                PagedList::getResults
        ) {
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entity) {
                database.routineDao().deleteAll();
                database.routineDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() < size) || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY));
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Routine> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<RoutineEntity>> loadFromDb() {
                return database.routineDao().findAll();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiService.getRoutines(search, difficulty, page, size, orderBy, direction);

            }
        }.asLiveData();


    }
    public LiveData<Resource<RoutineExecution>> executeRoutine (int routineId, RoutineExecution routineExecution){
        return new NetworkBoundResource<RoutineExecution,Void,RoutineExecution>(executors,v->null,v->null,v->v)
        {

            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable RoutineExecution model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RoutineExecution>> createCall() {
                return apiService.execute(routineId, routineExecution);
            }
        }.asLiveData();
    }
    public LiveData<Resource<List<RoutineExecution>>> getRoutineExecutions (int routineId, int page, int size, String orderBy, String dir){
        return new NetworkBoundResource<List<RoutineExecution>,Void,PagedList<RoutineExecution>>(executors,v->null,v->null, PagedList::getResults)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<RoutineExecution> model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineExecution>>> createCall() {
                return apiService.getRoutineExecutions(routineId,page,size,orderBy,dir);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Routine>> getRoutine(int routineId) {
        return new NetworkBoundResource<Routine, RoutineEntity,Routine>(executors,
                entity -> new Routine(entity.id,entity.name, entity.detail, entity.difficulty,new Category(entity.category_id,entity.category_name,entity.category_detail), entity.creatorId),
                routine ->
                new RoutineEntity(routine.getId(),routine.getDetail(),routine.getAverageRating(),routine.getName(),routine.getCreator().getId(),routine.getDifficulty(),routine.getCategory().getId(),routine.getCategory().getName(),routine.getCategory().getDetail()),
                model -> model
                ) {

            @Override
            protected void saveCallResult(@NonNull RoutineEntity entity) {
                database.routineDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable RoutineEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable Routine model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<RoutineEntity> loadFromDb() {
                return database.routineDao().findById(routineId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Routine>> createCall() {
                return apiService.getRoutine(routineId);
            }
        }.asLiveData();
    }


}
