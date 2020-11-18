package com.example.fitty.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.FittyApp;
import com.example.fitty.R;
import com.example.fitty.adapters.FavoriteAdapter;

import com.example.fitty.api.models.Error;
import com.example.fitty.api.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;

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
                routines = r.getData().getResults();
                adapter = new FavoriteAdapter(routines, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                orientationChange(gridLayoutManager, getActivity().getResources().getConfiguration());
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);
            } else {
                defaultResourceHandler(r);
            }
        });

        setTitle(getContext().getString(R.string.favoritas));
        setTopBar();

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
}