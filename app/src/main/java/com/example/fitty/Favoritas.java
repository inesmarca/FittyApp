package com.example.fitty;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.FavoriteAdapter;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Favoritas extends MainFragment implements FavoriteAdapter.OnFavoriteListener {
    View rootView;
    FavoriteAdapter adapter;
    GridLayoutManager gridLayoutManager;
    List<Routine> routines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favoritas, container, false);
        RecyclerView listView = rootView.findViewById(R.id.listFavoriteRoutine);
        FittyApp fitty = (FittyApp) getActivity().getApplication();
        fitty.getUserRepository().getUserFavourites(0,15,"name","asc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                if (!r.getData().getResults().isEmpty()) {
                    Log.d("ENTERED", "ENTERED");
                    routines = r.getData().getResults();
                    adapter = new FavoriteAdapter(routines, this);
                    gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    orientationChange(gridLayoutManager, getActivity().getResources().getConfiguration());
                    listView.setLayoutManager(gridLayoutManager);
                    listView.setAdapter(adapter);
                } else {
                    rootView.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.has_favorites).setVisibility(View.GONE);
                }
            } else {
                defaultResourceHandler(r);
            }
        });

        setTitle(getContext().getString(R.string.favoritas));
        setTopBar();

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_favorites_to_userProfile);
            }
        });

        return rootView;
    }

    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationChange(gridLayoutManager, newConfig);
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
        Navigation.findNavController(rootView).navigate(R.id.action_favorites_to_routineView, bundle);
    }
}