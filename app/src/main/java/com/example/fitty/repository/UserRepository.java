package com.example.fitty.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.fitty.api.ApiResponse;
import com.example.fitty.api.UserApiService;
import com.example.fitty.dbRoom.DB;
import com.example.fitty.api.models.EmailVerification;
import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Routine;
import com.example.fitty.api.models.RoutineExecution;
import com.example.fitty.api.models.Token;
import com.example.fitty.api.models.User;
import com.example.fitty.api.models.UserCredentials;
import com.example.fitty.dbRoom.entitys.UserEntity;

import java.util.concurrent.TimeUnit;

public class UserRepository {

    private final UserApiService apiService;


    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    private DB database;

    public UserRepository(AppExecutors executors, UserApiService service, DB database) {
        this.executors = executors;
        this.apiService = service;
        this.database = database;
    }

    private User mapUserEntityToDomain(UserEntity entity) {
        return new User(entity.id,entity.username,entity.fullName,entity.gender,entity.birthdate,entity.email
        ,null,null,null,null,false,true);

    }

    private UserEntity mapUserDomainToEntity(User user){
        return new UserEntity(user.getId(),user.getUsername(),user.getFullName(),user.getGender(),user.getBirthdate(),user.getEmail());
    }


    public LiveData<Resource<Token>> login(UserCredentials credentials) {
        return new NetworkBoundResource<Token, Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Token model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return apiService.login(credentials);
            }
        }.asLiveData();
    }


    public LiveData<Resource<Void>> logout() {
        return new NetworkBoundResource<Void, Void>
                (executors, null, null) {

            @Override
            protected void saveCallResult(@NonNull Void entity) {
            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Void model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.logout();
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> getCurrentUser() {
        return new NetworkBoundResource<User, UserEntity>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull UserEntity entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable User model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return apiService.getCurrentUser();
            }
        }.asLiveData();
    }



    public LiveData<Resource<PagedList<RoutineExecution>>> getUserExecutions(int page, int size, String orderBy, String direction) {
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
                return apiService.getUserExecutions(page,size,orderBy,direction);
            }
        }.asLiveData();
    }
    public LiveData<Resource<PagedList<Routine>>> getUserFavourites(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>,Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<Routine> model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiService.getUserFavourites(page,size,orderBy,direction);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> favRoutine(int routineId) {
        return new NetworkBoundResource<Void,Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Void model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>>createCall() {
                return apiService.markRoutineAsFavourite(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> unfavRoutine(int routineId) {
        return new NetworkBoundResource<Void,Void>(executors,null ,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Void model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>>createCall() {
                return apiService.unmarkRoutineAsFavourite(routineId);
            }
        }.asLiveData();
    }




    public LiveData<Resource<User>> addUser(User user) {
        return new NetworkBoundResource<User,Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable User model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return apiService.signup(user);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Void>> verify(EmailVerification emailVerification) {
        return new NetworkBoundResource<Void, Void>(executors,null,null)
        {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Void model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.verifyEmail(emailVerification);
            }
        }.asLiveData();


    }
    public LiveData<Resource<Void>> resendVerification(EmailVerification emailVerification) {
        return new NetworkBoundResource<Void, Void>(executors,null,null) {
            @Override
            protected void saveCallResult(@NonNull Void entity) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Void entity) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable Void model) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Void> loadFromDb() {
                return EmptyLiveData.create();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.resendVerification(emailVerification);
            }
        }.asLiveData();
    }

}