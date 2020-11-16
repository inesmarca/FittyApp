package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.UserApiService;
import com.example.fitty.models.EmailVerification;
import com.example.fitty.models.Token;
import com.example.fitty.models.User;
import com.example.fitty.models.UserCredentials;

public class UserRepository {

    private final UserApiService apiService;

    public UserRepository(Context context) {
        this.apiService = ApiClient.create(context, UserApiService.class);
    }

    public LiveData<Resource<Token>> login(UserCredentials credentials) {
        return new NetworkBoundResource<Token, Token>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return apiService.login(credentials);
            }
        }.asLiveData();
    }


    public LiveData<Resource<Void>> logout() {
        return new NetworkBoundResource<Void, Void>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.logout();
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> getCurrentUser() {
        return new NetworkBoundResource<User, User>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return apiService.getCurrentUser();
            }
        }.asLiveData();
    }
    public LiveData<Resource<User>> addUser(User user) {
        return new NetworkBoundResource<User, User>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return apiService.signup(user);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Void>> verify(EmailVerification emailVerification) {
        return new NetworkBoundResource<Void, Void>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.verifyEmail(emailVerification);
            }
        }.asLiveData();


    }
    public LiveData<Resource<Void>> resendVerification() {
        return new NetworkBoundResource<Void, Void>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.resendVerification();
            }
        }.asLiveData();
    }

}