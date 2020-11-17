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
import com.example.fitty.models.Category;
import com.example.fitty.models.Routine;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favoritas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favoritas extends Fragment {
    View rootView;
    FavoriteAdapter adapter;
    GridLayoutManager gridLayoutManager;

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


        ArrayList<Routine> routines = new ArrayList<>();
        routines.add(new Routine("Fuerza de Brazos", "45|Ejercicios de brazos", true, "Rookie", new Category(0, "cardio", "cardio")));
        routines.add(new Routine("Brazos", "25|Ejercicios de brazos", true, "Rookie", new Category(0, "tren superior", "cardio")));
        routines.add(new Routine("Relajación", "35|Ejercicios de brazos", true, "Rookie", new Category(0, "yoga", "yoga")));


        RecyclerView listView = rootView.findViewById(R.id.listFavoriteRoutine);
        adapter = new FavoriteAdapter(routines);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(adapter);

        return rootView;
    }

    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerView listView = rootView.findViewById(R.id.listFavoriteRoutine);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(4);
        } else {
            gridLayoutManager.setSpanCount(2);
        }
    }
}