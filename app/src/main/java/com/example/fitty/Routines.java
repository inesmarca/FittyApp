package com.example.fitty;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CategoriesAdapter;
import com.example.fitty.models.Category;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Routines#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Routines extends MainFragment implements CategoriesAdapter.OnCategoryListener {

    CategoriesAdapter adapter;
    List<Category> categories;
    View rootView;
    GridLayoutManager gridLayoutManager;
    SearchView searchView;
    ListView searchList;
    ListAdapter searchAdapter;
    List<Routine> searchResults = new ArrayList<>();

    // TODO: Rename and change types and number of parameters
    public static Routines newInstance(String param1, String param2) {
        Routines fragment = new Routines();
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
        rootView = inflater.inflate(R.layout.fragment_routines, container, false);

        RecyclerView listView = rootView.findViewById(R.id.listCategories);


        FittyApp fitty = (FittyApp) getActivity().getApplication();
        fitty.getCategoryRepository().getCategories(0,15,"name","asc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                categories = r.getData().getResults();
                adapter = new CategoriesAdapter(categories, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                orientationChange(gridLayoutManager, getActivity().getResources().getConfiguration());
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);
            } else {
                defaultResourceHandler(r);
            }
        });

        setTitle(getContext().getString(R.string.rutinas));
        setTopBar();

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_rutines_to_userProfile);
            }
        });

        searchView = rootView.findViewById(R.id.search);
        searchList = rootView.findViewById(R.id.searchList);
        searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchResults);
        searchList.setAdapter(searchAdapter);

        searchView.setOnClickListener(v -> {
            searchList.setVisibility(View.VISIBLE);
            searchList.bringToFront();
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchList.setVisibility(View.GONE);
                return false;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchList.setVisibility(View.GONE);
            }
        });

        searchView.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                searchList.setVisibility(View.GONE);
                return false;
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("routine", searchResults.get(position));
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                searchList.setVisibility(View.GONE);
                Navigation.findNavController(rootView).navigate(R.id.routineView, bundle);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList.setVisibility(View.VISIBLE);
                searchList.bringToFront();
                fitty.getRoutineRepository().getRoutines(query,null,0,10, null, null).observe(getActivity(),r->{
                    if (r.getStatus() == Status.SUCCESS) {
                        searchResults.clear();
                        searchResults = r.getData().getResults();
                        searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchResults);
                        searchList.setAdapter(searchAdapter);
                    } else {
                        defaultResourceHandler(r);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.setVisibility(View.VISIBLE);
                searchList.bringToFront();
                fitty.getRoutineRepository().getRoutines(newText,null,0,10, null, null).observe(getActivity(),r->{
                    if (r.getStatus() == Status.SUCCESS) {
                        searchResults.clear();
                        searchResults = r.getData().getResults();
                        searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchResults);
                        searchList.setAdapter(searchAdapter);
                    } else {
                        defaultResourceHandler(r);
                    }
                });
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void OnCategoryClick(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        Navigation.findNavController(rootView).navigate(R.id.category_routines, bundle);
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
        orientationChange(gridLayoutManager, newConfig);
    }
}