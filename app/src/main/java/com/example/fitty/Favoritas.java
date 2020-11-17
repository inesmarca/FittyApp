package com.example.fitty;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.FavoriteAdapter;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favoritas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favoritas extends Fragment implements FavoriteAdapter.OnFavoriteListener {
    View rootView;
    FavoriteAdapter adapter;
    GridLayoutManager gridLayoutManager;
    List<Routine> routines;

    public Favoritas() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Favoritas newInstance(String param1, String param2) {
        Favoritas fragment = new Favoritas();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favoritas, container, false);
        RecyclerView listView = rootView.findViewById(R.id.listFavoriteRoutine);

        FittyApp fitty = (FittyApp) getActivity().getApplication();
        fitty.getUserRepository().getUserFavourites(0,15,"name","asc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                routines = r.getData().getResults();
                adapter = new FavoriteAdapter(routines, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                orientationChange(getActivity().getResources().getConfiguration());
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);
            } else {
                defaultResourceHandler(r);
            }
        });

        return rootView;
    }

    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationChange(newConfig);
    }

    public void orientationChange(Configuration configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(4);
        } else {
            gridLayoutManager.setSpanCount(2);
        }
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
    public void OnFavoriteClick(Routine routine) {
        Fragment fragment = new RoutineView(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
    }
}