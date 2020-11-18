package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineView extends SecondaryFragment {

   /*Para llamar a la api laburamos con los repositorios. Hagan un getApplication y casteenlo
   a FittyApp, de ahi agarran el repo que necesiten y usan los métodos. Saludos :) */

    private View rootView;
    private MaterialToolbar toolbar;
    List<Cycle> cycles = new ArrayList<>();
    private CycleAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private Routine routine;

    public RoutineView(AllFragments fragment) {
        super(fragment);
        setTitle("");
    }

    // TODO: Rename and change types and number of parameters
    public static RoutineView newInstance(CategoryRoutines categoryRoutines) {
        RoutineView fragment = new RoutineView(categoryRoutines);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routine = (Routine) getArguments().getSerializable("routine");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_routine_view, container, false);

        RecyclerView listView = rootView.findViewById(R.id.listCycles);

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
            }
            else
                defaultResourceHandler(r);
        });

        ((TextView) rootView.findViewById(R.id.titRoutineView)).setText(routine.getName());
        ((Button) rootView.findViewById(R.id.buttonInitiate)).setOnClickListener(new buttonClick());
        setTopBar();

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
            startActivity(intent);
        }
    }
}