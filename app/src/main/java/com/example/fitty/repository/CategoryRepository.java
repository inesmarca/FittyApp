package com.example.fitty.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.CategoryApiService;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.dbRoom.entitys.CategoryEntity;

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


    public LiveData<Resource<List<Category>>> getCategories(int page, int size, String orderBy, String direction) {
        // dominio (view) - entidad(bd) - modelo (api). En la practica, dominio y model son lo mismo
        return new NetworkBoundResource<List<Category>, List<CategoryEntity>,PagedList<Category>>(executors,
                entities -> entities.stream().map(entity->
                                new Category(entity.id,entity.name,entity.detail))
                        .collect(toList()),
                domain -> domain.getResults().stream().map(category ->
                        new CategoryEntity(category.getId(),category.getName(),category.getDetail()))
                        .collect(toList()),
                model-> model.getResults()
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
    public LiveData<Resource<Category>> getCategory(int categoryId){
        // dominio (view) - entidad(bd) - modelo (api). En la practica, dominio y model son lo mismo

        return new NetworkBoundResource<Category, CategoryEntity,Category>(executors, entity ->
                new Category(entity.id,entity.name,entity.detail),
                category ->
                        new CategoryEntity(category.getId(),category.getName(),category.getDetail()),
                category -> category)

        {

            @Override
            protected void saveCallResult(@NonNull CategoryEntity entity) {
                database.categoryDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable CategoryEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable Category model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<CategoryEntity> loadFromDb() {
                return database.categoryDao().findById(categoryId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Category>> createCall() {
                return service.getCategory(categoryId);
            }
        }.asLiveData();
    }
}

