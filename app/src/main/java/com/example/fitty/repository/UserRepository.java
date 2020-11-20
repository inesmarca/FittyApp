package com.example.fitty.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.UserApiService;
import com.example.fitty.models.EmailVerification;
import com.example.fitty.models.PagedList;
import com.example.fitty.models.Routine;
import com.example.fitty.models.RoutineExecution;
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

    public LiveData<Resource<PagedList<RoutineExecution>>> getUserExecutions(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<RoutineExecution>,PagedList<RoutineExecution>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineExecution>>> createCall() {
                return apiService.getUserExecutions(page,size,orderBy,direction);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<Routine>>> getUserFavourites(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>,PagedList<Routine>>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiService.getUserFavourites(page,size,orderBy,direction);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> favRoutine(int routineId) {
        return new NetworkBoundResource<Void,Void>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>>createCall() {
                return apiService.markRoutineAsFavourite(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> unfavRoutine(int routineId) {
        return new NetworkBoundResource<Void,Void>()
        {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>>createCall() {
                return apiService.unmarkRoutineAsFavourite(routineId);
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