package com.example.fitty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.BestAdapter;
import com.example.fitty.adapters.RecentAdapter;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.models.RoutineExecution;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class Home extends MainFragment implements BestAdapter.OnRoutineListener, RecentAdapter.OnRoutineListener {

    private View rootView;
    private BestAdapter bestAdapter;
    private RecentAdapter recentAdapter;
    private List<Routine> listBest;
    private List<Routine> listRecent = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        FittyApp fitty = (FittyApp) getActivity().getApplication();

        RecyclerView recentRecyler = rootView.findViewById(R.id.listRecent);
        fitty.getUserRepository().getUserExecutions(0,10,"date","desc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData().getResults().isEmpty()) {
                    rootView.findViewById(R.id.imageHome).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.recentView).setVisibility(View.VISIBLE);
                    for (RoutineExecution routineExecution : r.getData().getResults()) {
                        listRecent.add(routineExecution.getRoutine());
                    }
                    recentAdapter = new RecentAdapter(listRecent, this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recentRecyler.setLayoutManager(linearLayoutManager);
                    rootView.findViewById(R.id.progressBar2).setVisibility(View.GONE);
                    recentRecyler.setAdapter(recentAdapter);
                }
            } else {
                defaultResourceHandler(r);
            }
        });

        RecyclerView bestRecycler = rootView.findViewById(R.id.listBest);
        fitty.getRoutineRepository().getRoutines(null,null,0,10, "averageRating", "desc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                listBest = r.getData().getResults();

                bestAdapter = new BestAdapter(listBest, this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                bestRecycler.setLayoutManager(linearLayoutManager);
                rootView.findViewById(R.id.progressBar3).setVisibility(View.GONE);
                rootView.findViewById(R.id.textDestacadas).setVisibility(View.VISIBLE);
                bestRecycler.setAdapter(bestAdapter);
            } else {
                defaultResourceHandler(r);
            }
        });

        setTitle(getActivity().getResources().getString(R.string.home));
        setTopBar();

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_home_to_userProfile);
            }
        });

        return rootView;
    }

    private void defaultResourceHandler(Resource<?> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                Log.d("UI", getString(R.string.loading));

                break;
            case ERROR:
                Error error = resource.getError();
                String message = getString(R.string.error, error.getDescription(), error.getCode());
                Log.d("UI", message);
                break;
        }
    }

    @Override
    public void OnRoutineClick(Routine routine) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        Navigation.findNavController(rootView).navigate(R.id.action_home_to_routineView, bundle);
    }
}