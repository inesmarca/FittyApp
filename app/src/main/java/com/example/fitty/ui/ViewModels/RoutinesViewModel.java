package com.example.fitty.ui.ViewModels;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.fitty.api.models.PagedList;
import com.example.fitty.api.models.Routine;
import com.example.fitty.repository.EmptyLiveData;
import com.example.fitty.repository.RepoViewModel;
import com.example.fitty.repository.RepoViewModelFactory;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.RoutineRepository;
import com.example.fitty.repository.Status;
import com.example.fitty.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class RoutinesViewModel extends RepoViewModel<RoutineRepository> {

    private final static int PAGE_SIZE = 10;

    private int page = 0;
    private boolean isLastPage = false;
    private final List<Routine> allRoutines = new ArrayList<>();
    private final MediatorLiveData<Resource<List<Routine>>> routines = new MediatorLiveData<>();
    private final LiveData<Resource<Routine>> routine;
    private  MainActivity activity;


    public RoutinesViewModel(RoutineRepository repository, MainActivity activity) {
        super(repository);

        MutableLiveData<Integer> routineId1 = new MutableLiveData<>();
        routine = Transformations.switchMap(routineId1, routineId -> {
            if (routineId == null) {
                return EmptyLiveData.create();
            } else {
                return repository.getRoutine(activity,routineId);
            }
        });

    }

    public LiveData<Resource<List<Routine>>> getRoutines(MainActivity activity) {
        getMoreRoutines(activity);
        return routines;
    }

    public void getMoreRoutines(MainActivity activity) {
        if (isLastPage)
            return;

        routines.addSource(repository.getRoutines(activity,null,null,page, PAGE_SIZE,null,null),resource -> {
            if (resource.getStatus() == Status.SUCCESS) {
                if ((resource.getData().getResults().size() == 0) || (resource.getData().getResults().size() < PAGE_SIZE))
                    isLastPage = true;

                page++;

                allRoutines.addAll(resource.getData().getResults());
                routines.setValue(Resource.success(allRoutines));
            } else if (resource.getStatus() == Status.LOADING) {
                routines.setValue(new Resource<>(Status.LOADING,resource.getData().getResults(),resource.getError()));
            }
        });
    }
    public LiveData<Resource<Routine>> getRoutine() {
        return routine;
    }

}
