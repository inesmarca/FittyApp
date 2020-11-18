package com.example.fitty.repository;



import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.fitty.api.ApiResponse;

import java.util.function.Function;


public abstract class NetworkBoundResource<DomainType, EntityType> {
    private final AppExecutors appExecutors;
    private final Function<EntityType, DomainType> mapEntityToDomain;
    private final Function<DomainType, EntityType> mapDomainToEntity;



    private final MediatorLiveData<Resource<DomainType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors, Function<EntityType, DomainType> mapEntityToDomain, Function<DomainType, EntityType> mapDomainToEntity) {
        this.appExecutors = appExecutors;
        this.mapEntityToDomain = mapEntityToDomain;
        this.mapDomainToEntity = mapDomainToEntity;


        result.setValue(Resource.loading(null));
        LiveData<EntityType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> {
                    DomainType domain = mapEntityToDomain.apply(newData);
                    setValue(Resource.success(domain));
                });
            }
        });
    }

    @MainThread
    private void setValue(Resource<DomainType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<EntityType> dbSource) {
        LiveData<ApiResponse<DomainType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource,
                newData -> {
                    DomainType domain = (newData != null) ?
                            mapEntityToDomain.apply(newData) :
                            null;
                    setValue(Resource.loading(domain));
                }
        );
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            if (response.getError() != null) {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> {
                            DomainType domain = (newData != null) ?
                                    mapEntityToDomain.apply(newData) :
                                    null;
                            setValue(Resource.error(response.getError(), domain));
                        }
                );
            } else /*if (response.getData() != null)*/ {
                DomainType model = processResponse(response);
                if (shouldPersist(model)) {
                    appExecutors.diskIO().execute(() -> {
                        EntityType entity = mapDomainToEntity.apply(model);
                        saveCallResult(entity);
                        appExecutors.mainThread().execute(() ->
                                // we specially request a new live data,
                                // otherwise we will get immediately last cached value,
                                // which may not be updated with latest results received from network.
                                result.addSource(loadFromDb(),
                                        newData -> {
                                            DomainType domain = (newData != null) ?
                                                    mapEntityToDomain.apply(newData) :
                                                    model;
                                            setValue(Resource.success(domain));
                                        })
                        );
                    });
                } else {
                    appExecutors.mainThread().execute(() -> {
                        setValue(Resource.success(model));

                    });
                }
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<DomainType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected DomainType processResponse(ApiResponse<DomainType> response) {
        return response.getData();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull EntityType entity);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable EntityType entity);

    @MainThread
    protected abstract boolean shouldPersist(@Nullable DomainType model);

    @NonNull
    @MainThread
    protected abstract LiveData<EntityType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<DomainType>> createCall();
}