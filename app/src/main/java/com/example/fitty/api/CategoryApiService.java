package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.PagedList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryApiService {

    @GET("categories")
    LiveData<ApiResponse<PagedList<Category>>> getCategories(@Query("page") int page,@Query("size")int size,@Query("orderBy")String orderBy,@Query("direction") String direction);

    @GET("categories/{categoryId}")
    LiveData<ApiResponse<Category>> getCategory(@Path("categoryId")int categoryId);

}
