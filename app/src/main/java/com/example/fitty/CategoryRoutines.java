package com.example.fitty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CategoryRoutinesAdapter;
import com.example.fitty.models.Category;
import com.example.fitty.models.Routine;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryRoutines#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryRoutines extends Fragment {

    private int idCategory;
    private CategoryRoutinesAdapter adapter;

    public CategoryRoutines() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CategoryRoutines newInstance(String param1, String param2) {
        CategoryRoutines fragment = new CategoryRoutines();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCategory = getArguments().getInt("idCategory");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_routines, container, false);

        ArrayList<Routine> routines = new ArrayList<>();
        routines.add(new Routine("Fuerza de Brazos", "45|Ejercicios de brazos", true, "Rookie", new Category(0, "cardio", "cardio")));
        routines.add(new Routine("Brazos", "25|Ejercicios de brazos", true, "Rookie", new Category(0, "tren superior", "cardio")));
        routines.add(new Routine("Relajación", "35|Ejercicios de brazos", true, "Rookie", new Category(0, "yoga", "yoga")));


        RecyclerView listView = rootView.findViewById(R.id.listCategoryRoutines);
        adapter = new CategoryRoutinesAdapter(routines);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listView.setAdapter(adapter);
        return rootView;
    }
}