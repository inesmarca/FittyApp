package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.EmailVerification;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;
import com.example.fitty.models.RoutineExecution;
import com.example.fitty.models.Token;
import com.example.fitty.models.User;
import com.example.fitty.models.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiService {

    @POST("user/login")
    LiveData<ApiResponse<Token>> login(@Body UserCredentials credentials);
    @POST("user/logout")
    LiveData<ApiResponse<Void>> logout();
    @GET("user/current")
    LiveData<ApiResponse<User>> getCurrentUser();

    @POST("user")
    LiveData<ApiResponse<User>> signup(@Body User user);

    @POST("user/resend_verification")
    LiveData<ApiResponse<Void>> resendVerification(@Body EmailVerification emailVerification);

    @POST("user/verify_email")
    LiveData<ApiResponse<Void>> verifyEmail(@Body EmailVerification emailVerification);


    @GET("user/current/routines/executions")
    LiveData<ApiResponse<PagedList<RoutineExecution>>> getUserExecutions( @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);

    @GET("user/current/routines/favourites")
    LiveData<ApiResponse<PagedList<Routine>>> getUserFavourites( @Query("page")int page, @Query("size")int size, @Query("orderBy")String orderBy, @Query("direction")String direction);

    @POST("user/current/routines/{routineId}/favourites")
    LiveData<ApiResponse<Void>> markRoutineAsFavourite(@Path("routineId")int routineId);

    @DELETE("user/current/routines/{routineId}/favourites")
    LiveData<ApiResponse<Void>> unmarkRoutineAsFavourite(@Path("routineId")int routineId);


}
