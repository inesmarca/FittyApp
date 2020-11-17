package com.example.fitty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CycleAdapter;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Routine;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineView extends Fragment {

    private View rootView;
    private MaterialToolbar toolbar;
    List<Cycle> cycles;
    private CycleAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private Routine routine;

    public RoutineView() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RoutineView newInstance(String param1, String param2) {
        RoutineView fragment = new RoutineView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routine = (Routine) getArguments().getSerializable("routine");
            toolbar = requireActivity().findViewById(R.id.topAppBar);
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_routine_view, container, false);

        RecyclerView listView = rootView.findViewById(R.id.listCycles);

        cycles = new ArrayList<>();
        cycles.add(new Cycle(0, "Calentamiento", "Calentamiento", "warmup", 1, 30));
        cycles.add(new Cycle(1, "Ciclo 1", "Calentamiento", "warmup", 1, 30));
        cycles.add(new Cycle(2, "Enfriamiento", "Calentamiento", "warmup", 1, 30));

        adapter = new CycleAdapter(cycles);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(adapter);
        ((TextView) rootView.findViewById(R.id.titRoutineView)).setText(routine.getName());

        return rootView;
    }
}