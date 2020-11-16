package com.example.fitty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CategoryRoutinesAdapter;
import com.example.fitty.models.Category;
import com.example.fitty.models.Routine;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryRoutines#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryRoutines extends Fragment implements View.OnClickListener {

    private int idCategory;
    private String nameCategory;
    private MaterialToolbar toolbar;
    private CategoryRoutinesAdapter adapter;
    String[] orderTypes = {"Mejor Rating", "Más Recientes", "Dificultad"};
    View rootView;

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
            nameCategory = getArguments().getString("titCategory");
            toolbar = requireActivity().findViewById(R.id.topAppBar);
            toolbar.setTitle(nameCategory);
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            toolbar.setOnClickListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category_routines, container, false);

        ArrayList<Routine> routines = new ArrayList<>();
        routines.add(new Routine("Fuerza de Brazos", "45|Ejercicios de brazos", true, "Rookie", new Category(0, "cardio", "cardio")));
        routines.add(new Routine("Brazos", "25|Ejercicios de brazos", true, "Rookie", new Category(0, "tren superior", "cardio")));
        routines.add(new Routine("Relajación", "35|Ejercicios de brazos", true, "Rookie", new Category(0, "yoga", "yoga")));


        RecyclerView listView = rootView.findViewById(R.id.listCategoryRoutines);
        adapter = new CategoryRoutinesAdapter(routines);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listView.setAdapter(adapter);

        Spinner orderSpinner = rootView.findViewById(R.id.order_spinner);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Si i == 0 entonces no selecciono un genero aun
                /*if(i != 0) {
                    //A completar
                    // en genders[i] esta el sexo seleccionado
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //A completar
            }
        });

        ArrayAdapter gendersAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, orderTypes); //android.R.layout.simple_spinner_item
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(gendersAdapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        toolbar.setNavigationIcon(R.drawable.ic_account_circle);
        toolbar.setOnClickListener(null);
        toolbar.setTitle(R.string.rutinas);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, new Routines()).commit();
    }
}