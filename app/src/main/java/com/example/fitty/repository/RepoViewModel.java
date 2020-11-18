package com.example.fitty.repository;

import androidx.lifecycle.ViewModel;

public class RepoViewModel<T> extends ViewModel {
    protected T repository;

    public RepoViewModel(T repository) {
        this.repository = repository;
    }
}
