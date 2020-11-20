package com.example.fitty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CycleAdapter;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Error;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class RoutineView extends SecondaryFragment {

   /*Para llamar a la api laburamos con los repositorios. Hagan un getApplication y casteenlo
   a FittyApp, de ahi agarran el repo que necesiten y usan los métodos. Saludos :) */

    private View rootView;
    private MaterialToolbar toolbar;
    List<Cycle> cycles = new ArrayList<>();
    private CycleAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private Routine routine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routine = (Routine) getArguments().getSerializable("routine");
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_routine_view, container, false);

        RecyclerView listView = rootView.findViewById(R.id.listCycles);
        ((TextView) rootView.findViewById(R.id.titRoutineView)).setText(routine.getName());
        ((RatingBar) rootView.findViewById(R.id.ratingRoutineView)).setRating(routine.getRating());
        ((TextView) rootView.findViewById(R.id.duration)).setText(routine.getDuration() + "'");
        Button buttonInitiate = rootView.findViewById(R.id.buttonInitiate);
        buttonInitiate.setEnabled(false);
        buttonInitiate.setOnClickListener(new buttonClick());

        FittyApp fittyApp = (FittyApp) getActivity().getApplication();
        fittyApp.getCycleRepository().getRoutineCycles(routine.getId(), 0, 99, "order", "asc").observe(getActivity(),r->{
            if(r.getStatus()== Status.SUCCESS){
                assert r.getData() != null;
                cycles = r.getData().getResults();

                if (cycles != null) {
                    routine.addCycle(cycles);
                }

                adapter = new CycleAdapter(cycles, this);
                gridLayoutManager = new GridLayoutManager(getContext(), 1);
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);

                buttonInitiate.setEnabled(true);
            }
            else
                defaultResourceHandler(r);
        });

        setTopBar();
        toolbar = getActivity().findViewById(R.id.topAppBar);
        getActivity().getMenuInflater().inflate(R.menu.top_bar, toolbar.getMenu());


        toolbar.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SHARE", "CLICKED");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.fitty.com/id/" + routine.getId());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        toolbar.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                Log.d("LIKE", "CLICKED");
                FittyApp fitty = (FittyApp) getActivity().getApplication();
                if (!routine.isFavorite()) {
                    fitty.getUserRepository().favRoutine(routine.getId()).observe(getActivity(),r-> toolbar.getMenu().getItem(1).setIcon(R.drawable.ic_favorite_full));
                } else {
                    fitty.getUserRepository().unfavRoutine(routine.getId()).observe(getActivity(),r-> toolbar.getMenu().getItem(1).setIcon(R.drawable.ic_favorite_border_black));
                }

            }
        });

        return rootView;
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

    public Routine getRoutine() {
        return routine;
    }

    public class buttonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ExecuteActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("routine", routine);
            intent.putExtras(bundle);
            startActivityForResult(intent, 3);
        }
    }

    public void onClick(View v) {
        toolbar.getMenu().clear();
        Navigation.findNavController(rootView).navigateUp();
    }
}