package com.example.fitty.ui.ViewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

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

    private int local_page = 0;
    private boolean isLastPage = false;
    private final List<Routine> allRoutines = new ArrayList<>();
    private final MediatorLiveData<Resource<List<Routine>>> routines = new MediatorLiveData<>();
    private final LiveData<Resource<Routine>> routine;


    public RoutinesViewModel(RoutineRepository repository){
        super(repository);

        MutableLiveData<Integer> routineId1 = new MutableLiveData<>();
        routine = Transformations.switchMap(routineId1, routineId -> {
            if (routineId == null) {
                return EmptyLiveData.create();
            } else {
                return repository.getRoutine(routineId);
            }
        });

    }

    public LiveData<Resource<List<Routine>>> getRoutines( String search, String diff, int page, int size, String orderBy, String dir) {
        getMoreRoutines( search, diff, page, size, orderBy, dir);
        return routines;
    }

    public void getMoreRoutines(String search, String diff, Integer page, Integer size, String orderBy, String dir) {
        if (isLastPage)
            return;

        routines.addSource(repository.getRoutines(search,diff,page != null ? page: local_page , size !=null ? size : PAGE_SIZE ,orderBy,dir),resource -> {
            if (resource.getStatus() == Status.SUCCESS) {

                if(page == null) {
                 //Puede pedirme algo especifico, em ese caso no entraria a este if
                    local_page++;
                    isLastPage =
                    isLastPage = ((resource.getData().size() == 0) || (resource.getData().size() < PAGE_SIZE));

                }

                assert resource.getData() != null;
                allRoutines.addAll(resource.getData());
                routines.setValue(Resource.success(allRoutines));
            } else if (resource.getStatus() == Status.LOADING) {

                routines.setValue(resource);
            }
        });
    }
    public LiveData<Resource<Routine>> getRoutine() {
        return routine;
    }

}
