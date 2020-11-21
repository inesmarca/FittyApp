package com.example.fitty;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CategoryRoutinesAdapter;
import com.example.fitty.models.Category;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryRoutines extends SecondaryFragment implements CategoryRoutinesAdapter.OnCategoryRoutineListener {

    private Category category;
    private MaterialToolbar toolbar;
    private CategoryRoutinesAdapter adapter;
    View rootView;
    List<Routine> routines = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    FittyApp fittyApp;
    RecyclerView listView;

    Order[] orders;
    String[] orderTypes;
    Order selectedOrder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = (Category) getArguments().getSerializable("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category_routines, container, false);

        listView = rootView.findViewById(R.id.listCategoryRoutines);

        fittyApp = (FittyApp) getActivity().getApplication();

        orders = new Order[]{
                new Order("averageRating", "desc", getString(R.string.best)),
                new Order("date", "desc", getString(R.string.recent)),
                new Order("difficulty", "asc", getString(R.string.leastDificulty)),
                new Order("difficulty", "desc", getString(R.string.moreDificulty))
        };
        orderTypes = new String[orders.length];

        Integer i = 0;
        for (; i < orders.length; i++) {
            orderTypes[i] = orders[i].title;
        }

        getRoutines(null, null);

        ((TextInputLayout) rootView.findViewById(R.id.txtOrder)).getEditText().setOnClickListener(v -> {
            selectOrder();
        });

        setTopBar();

        return rootView;
    }

    private void selectOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.hint_order));
        builder.setItems(orderTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOrder = orders[which];
                ((TextInputLayout) rootView.findViewById(R.id.txtOrder)).getEditText().setText(orders[which].title);
                getRoutines(selectedOrder.orderBy, selectedOrder.direction);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
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

    @Override
    public void OnCategoryRoutineClick(Routine routine) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        Navigation.findNavController(rootView).navigate(R.id.action_categoryRoutines_to_routineView, bundle);
    }

    private void getRoutines(String orderBy, String direction) {
        fittyApp.getRoutineRepository().getRoutines(null,null,0,200, orderBy, direction).observe(getActivity(),r->{
            if(r.getStatus()== Status.SUCCESS){
                assert r.getData() != null;
                routines.clear();
                routines.addAll(r.getData().getResults());
                routines.removeIf(routine ->
                        routine.getCategory().getId() != category.getId()
                );
                Log.d("IDD", String.format("%d", category.getId()));

                adapter = new CategoryRoutinesAdapter(routines, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                orientationChange(gridLayoutManager, getActivity().getResources().getConfiguration());
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else
                defaultResourceHandler(r);
        });
    }
}













