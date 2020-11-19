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

    public LiveData<Resource<PagedList<Routine>>> getRoutines(MainActivity activity, String search, String difficulty, int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>,List<RoutineEntity>>(executors,
                entities ->

                        new PagedList<>(0, orderBy, direction,
                                entities.stream().map(
                                        entity -> {
                                            //SINCRONIZAR PARA PODER ENVIAR OBJETO CATEGORY EN VEZ DE ID
                                  /*          LiveData<CategoryEntity> hola = database.categoryDao().findById(entity.categoryId);

                                            hola.observe(activity,categoryEntity -> {
                                                   Category category = parseCategory(categoryEntity);
                                                    System.out.println(category.getName());

                                            });*/
                                            System.out.println("ESTOY ACA");
                                            categoryRepository.getCategory(entity.categoryId).observe(activity,v->{
                                                if(v.getStatus()==Status.SUCCESS){
                                                    category=v.getData();
                                          //          semaphore.release();
                                                }
                                                else
                                                    defaultResourceHandler(v);

                                            });


                                            return new Routine(entity.name, entity.detail, entity.difficulty,category, entity.creatorId);

                                        }

                                ).collect(toList())
                                , size, page, false),
                domain ->
                        domain.getResults().stream().map(routine-> new RoutineEntity(routine.getId(),routine.getDetail(),routine.getAverageRating(),routine.getName(),routine.getCreator().getId(),routine.getCategory().getId(),routine.getDifficulty()))
                                .collect(toList())) {


            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entity) {
                database.routineDao().deleteAll();
                database.routineDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0) || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY));
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
        return new NetworkBoundResource<RoutineExecution,RoutineExecution>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull RoutineExecution entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable RoutineExecution entity) {
                return false;
            }

            @Override
            protected boolean shouldPersist(@Nullable RoutineExecution model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<RoutineExecution> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RoutineExecution>> createCall() {
                return apiService.execute(routineId,routineExecution);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<RoutineExecution>>> getRoutineExecutions (int routineId, int page, int size, String orderBy, String dir){
        return new NetworkBoundResource<PagedList<RoutineExecution>,Void>(executors,null,null)
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
    public LiveData<Resource<Routine>> getRoutine(MainActivity activity, int routineId) {
        return new NetworkBoundResource<Routine, RoutineEntity>(executors, entity -> {
            categoryRepository.getCategory(entity.categoryId).observe(activity,v->{
                if(v.getStatus()==Status.SUCCESS){
                    _category = v.getData();
                }
                else
                    defaultResourceHandler(v);
            });
            return new Routine(entity.name, entity.detail, entity.difficulty,_category, entity.creatorId);

        },
                routine ->
                new RoutineEntity(routine.getId(),routine.getDetail(),routine.getAverageRating(),routine.getName(),routine.getCreator().getId(),routine.getCategory().getId(),routine.getDifficulty())) {

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
    }    private Category parseCategory(CategoryEntity categoryEntity){
        return new Category(categoryEntity.id,categoryEntity.name,categoryEntity.detail);
    }

    private void defaultResourceHandler(Resource<?> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                Log.d("UI", "CARGANDO");

                break;
            case ERROR:
                Error error = resource.getError();
                String message =  error.getDescription() + error.getCode();
                Log.d("UI", message);
                break;
        }
    }
}
