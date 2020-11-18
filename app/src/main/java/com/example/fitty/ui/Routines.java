package com.example.fitty.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.FittyApp;
import com.example.fitty.R;
import com.example.fitty.adapters.CategoriesAdapter;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.Error;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;

import org.jetbrains.annotations.NotNull;

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

        return rootView;
    }

    @Override
    public void OnCategoryClick(Category category) {
        Fragment fragment = new CategoryRoutines(this, category.getName());
        Bundle bundle = new Bundle();
        bundle.putInt("idCategory", category.getId());
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
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