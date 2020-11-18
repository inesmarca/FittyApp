package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.CategoryApiService;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.Cycle;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.dbRoom.entitys.CategoryEntity;
import com.example.fitty.dbRoom.entitys.CycleEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;


public class CategoryRepository {

    private AppExecutors executors;
    private CategoryApiService service;
    private DB database;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    public CategoryRepository(AppExecutors executors, CategoryApiService service, DB database) {
        this.service = service;
        this.database=database;
        this.executors=executors;
    }

    public LiveData<Resource<PagedList<Category>>> getCategories(int page,int size,String orderBy,String direction) {
        return new NetworkBoundResource<PagedList<Category>, List<CategoryEntity>>(executors,
                entities -> new PagedList<>(0,orderBy,direction,
                        entities.stream().map(entity->
                                new Category(entity.id,entity.name,entity.detail))
                        .collect(toList()), page,size,false),
                domain -> domain.getResults().stream().map(category ->
                        new CategoryEntity(category.getId(),category.getName(),category.getDetail()))
                        .collect(toList())
        )
        {


            @Override
            protected void saveCallResult(@NonNull List<CategoryEntity> entities) {

                database.categoryDao().delete(size, page * size);
                database.categoryDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryEntity> entities) {
                return ((entities == null) || (entities.size() == 0));

            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Category> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryEntity>> loadFromDb() {
                return database.categoryDao().findAll(size, page * size);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Category>>> createCall() {
                return service.getCategories(page, size, orderBy, direction);
            }
        }.asLiveData();
    }
}

