package com.example.fitty.repository;
import androidx.lifecycle.LiveData;

public class EmptyLiveData extends LiveData {
    private EmptyLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {

        return new EmptyLiveData();
    }
}