package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.CategoryApiService;
import com.example.fitty.models.Category;
import com.example.fitty.models.PagedList;


public class CategoryRepository {

    private final CategoryApiService apiService;

    public CategoryRepository(Context context) {
        this.apiService = ApiClient.create(context, CategoryApiService.class);
    }

    public LiveData<Resource<PagedList<Category>>> getCategories(int page,int size,String orderBy,String direction) {
        return new NetworkBoundResource<PagedList<Category>, PagedList<Category>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Category>>> createCall() {
                return apiService.getCategories(page,size,orderBy,direction);
            }
        }.asLiveData();
    }
}

