package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import com.example.fitty.models.EmailVerification;
import com.example.fitty.models.Token;
import com.example.fitty.models.User;
import com.example.fitty.models.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
    LiveData<ApiResponse<Void>> resendVerification();

    @POST("user/verify_email")
    LiveData<ApiResponse<Void>> verifyEmail(@Body EmailVerification emailVerification);






}
