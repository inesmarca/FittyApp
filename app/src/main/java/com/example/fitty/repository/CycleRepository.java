package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.CycleApiService;
import com.example.fitty.api.UserApiService;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.Cycle;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.dbRoom.entitys.CycleEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CycleRepository {
    private  CycleApiService apiService;
    private DB database;
    private AppExecutors executors;

    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";
    public CycleRepository(AppExecutors executors, CycleApiService service, DB database) {
        this.executors = executors;
        this.apiService = service;
        this.database = database;
    }

    public LiveData<Resource<List<Cycle>>> getRoutineCycles (int routineId,int page,int size,String orderBy,String direction){
        return new NetworkBoundResource<List<Cycle>, List<CycleEntity>, PagedList<Cycle>>(executors,

                entities -> entities.stream().map(entity -> new Cycle(entity.id,entity.name,entity.detail,entity.type,entity.order,entity.repetitions))
                .collect(toList()),
                domain -> domain.getResults().stream().map(cycle ->
                        new CycleEntity(cycle.getId(),cycle.getName(),cycle.getDetail(),cycle.getType(),cycle.getOrder(),cycle.getRepetitions()))
                        .collect(toList())
                ,
                PagedList::getResults)

        {
            @Override
            protected void saveCallResult(@NonNull List<CycleEntity> entities) {
                database.cycleDao().deleteAll();
                database.cycleDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CycleEntity> entities) {
                return ((entities == null) || (entities.size() == 0) || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY));
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Cycle> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CycleEntity>> loadFromDb() {
                return database.cycleDao().findAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Cycle>>> createCall() {
                return apiService.getRoutineCycles(routineId,page,size,orderBy,direction);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Cycle>> getRoutineCycle(int routineId,int cycleId){
        return new NetworkBoundResource<Cycle,CycleEntity,Cycle>(executors,
                entity -> new Cycle(entity.id,entity.name,entity.detail,entity.type,entity.order,entity.repetitions),

                domain -> new CycleEntity(domain.getId(),domain.getName(),domain.getDetail(),domain.getType(),domain.getOrder(),domain.getRepetitions()),

                model -> model

                )
        {
            @Override
            protected void saveCallResult(@NonNull CycleEntity entity) {
                database.cycleDao().insert(entity);

            }

            @Override
            protected boolean shouldFetch(@Nullable CycleEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable Cycle model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<CycleEntity> loadFromDb() {
                return database.cycleDao().findById(cycleId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Cycle>> createCall() {
                return apiService.getRoutineCycle(routineId,cycleId);
            }
        }.asLiveData();
    }
}
