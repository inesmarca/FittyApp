package com.example.fitty;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryRoutines#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryRoutines extends Fragment implements View.OnClickListener, CategoryRoutinesAdapter.OnCategoryRoutineListener {

    private int idCategory;
    private String nameCategory;
    private MaterialToolbar toolbar;
    private CategoryRoutinesAdapter adapter;
    String[] orderTypes = {"Mejor Rating", "Más Recientes", "Dificultad"};
    View rootView;
    List<Routine> routines;
    GridLayoutManager gridLayoutManager;

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
        View rootView = inflater.inflate(R.layout.fragment_category_routines, container, false);

        RecyclerView listView = rootView.findViewById(R.id.listCategoryRoutines);

        FittyApp fittyApp = (FittyApp) getActivity().getApplication();
        fittyApp.getRoutineRepository().getRoutines(null,"rookie",0,100,"averageRating","asc").observe(getActivity(),r->{
            if(r.getStatus()== Status.SUCCESS){
                assert r.getData() != null;
                routines = r.getData().getResults();
                routines.removeIf(routine ->
                    routine.getCategory().getId()!=idCategory
                );
                Log.d("IDD", String.format("%d",idCategory));

                adapter = new CategoryRoutinesAdapter(routines, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);
            }
            else
                defaultResourceHandler(r);
        });


 

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

    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerView listView = rootView.findViewById(R.id.listCategoryRoutines);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(4);
        } else {
            gridLayoutManager.setSpanCount(2);
        }
    }

    @Override
    public void OnCategoryRoutineClick(Routine routine) {
        Fragment fragment = new RoutineView();
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
    }
}













