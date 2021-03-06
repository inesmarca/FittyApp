package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.Category;
import com.example.fitty.models.PagedList;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryApiService {

    @GET("categories")
    LiveData<ApiResponse<PagedList<Category>>> getCategories(@Query("page") int page,@Query("size")int size,@Query("orderBy")String orderBy,@Query("direction") String direction);
}
