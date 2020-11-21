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

import com.example.fitty.adapters.FavoriteAdapter;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Favoritas extends MainFragment implements FavoriteAdapter.OnFavoriteListener {
    View rootView;
    FavoriteAdapter adapter;
    GridLayoutManager gridLayoutManager;
    List<Routine> routines;
    FittyApp fittyApp;
    RecyclerView listView;

    Order[] orders;
    String[] orderTypes;
    Order selectedOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favoritas, container, false);
        listView = rootView.findViewById(R.id.listFavoriteRoutine);
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

        ((TextInputLayout) rootView.findViewById(R.id.favTxtOrder)).getEditText().setOnClickListener(v -> {
            selectOrder();
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

    private void selectOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.hint_order));
        builder.setItems(orderTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOrder = orders[which];
                ((TextInputLayout) rootView.findViewById(R.id.favTxtOrder)).getEditText().setText(orders[which].title);
                getRoutines(selectedOrder.orderBy, selectedOrder.direction);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
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

    private void getRoutines(String orderBy, String direction) {
        fittyApp.getUserRepository().getUserFavourites(0,15, orderBy, direction).observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                if (!r.getData().getResults().isEmpty()) {
                    routines = r.getData().getResults();
                    adapter = new FavoriteAdapter(routines, this);
                    gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    orientationChange(gridLayoutManager, getActivity().getResources().getConfiguration());
                    listView.setLayoutManager(gridLayoutManager);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    rootView.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.has_favorites).setVisibility(View.GONE);
                }
            } else {
                defaultResourceHandler(r);
            }
        });
    }
}