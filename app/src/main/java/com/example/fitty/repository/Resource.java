package com.example.fitty.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.fitty.api.models.Error;

import static com.example.fitty.repository.Status.ERROR;
import static com.example.fitty.repository.Status.LOADING;
import static com.example.fitty.repository.Status.SUCCESS;

public class Resource<T> {

    @NonNull
    private final Status status;

    @Nullable
    private final Error error;

    @Nullable
    private final T data;

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public Resource(@NonNull Status status, @Nullable T data, @Nullable Error error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Error error, @Nullable T data) {
        return new Resource<T>(ERROR, data, error);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}