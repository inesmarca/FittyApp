package com.example.fitty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitty.models.Error;
import com.example.fitty.models.RoutineExecution;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Progress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Progress extends MainFragment {

    View rootView;
    List<RoutineExecution> routineExecutions;
    Map<Integer, Integer> dateExectutions = new HashMap<>();

    public Progress() {}

    // TODO: Rename and change types and number of parameters
    public static Progress newInstance(String param1, String param2) {
        Progress fragment = new Progress();
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
        rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        setTitle(getContext().getString(R.string.progreso));
        setTopBar();

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_progress_to_userProfile);
            }
        });

        BarChart chart = rootView.findViewById(R.id.barChart);
        List<BarEntry> data = new ArrayList<>();


        FittyApp fitty = (FittyApp) getActivity().getApplication();
        fitty.getUserRepository().getUserExecutions(0,50,"date","desc").observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData().getResults().isEmpty()) {
                    // Mostrar mensaje
                } else {
                    routineExecutions = r.getData().getResults();
                    for (RoutineExecution routine : routineExecutions) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                        Integer date = Integer.valueOf(simpleDateFormat.format(routine.getDate()));
                        Log.d("-----VALUE------", date.toString());

                        if (dateExectutions.containsKey(date)) {
                            Integer value = dateExectutions.get(date);
                            dateExectutions.replace(date, value + 1);
                            Log.d("-----VALUE------", dateExectutions.get(date).toString());
                        } else {
                            dateExectutions.put(date, 1);
                        }
                    }

                    dateExectutions.forEach((k, v) -> data.add(new BarEntry(k, v)));

                    BarDataSet dataSet = new BarDataSet(data, "Values");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextColor(R.color.design_default_color_secondary_variant);
                    dataSet.setValueTextSize(18f);

                    BarData barData = new BarData(dataSet);
                    chart.setFitBars(true);
                    barData.setBarWidth(2f);
                    chart.setData(barData);
                    chart.getDescription().setText("Bar Chart of Activity");
                    chart.animateY(2000);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }
            } else {
                defaultResourceHandler(r);
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
}