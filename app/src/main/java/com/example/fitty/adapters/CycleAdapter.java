package com.example.fitty.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.FittyApp;
import com.example.fitty.R;
import com.example.fitty.RoutineView;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Error;
import com.example.fitty.models.Exercise;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;

import java.util.List;

public class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.CycleViewHolder> {

       /*Para llamar a la api laburamos con los repositorios. Hagan un getApplication y casteenlo
   a FittyApp, de ahi agarran el repo que necesiten y usan los métodos. Saludos :) */

    //Como aca estas queriendo hacerlo desde un adapter, para el getApplication vas a tener q hacer cositas

    private List<Cycle> data;
    View view;
    ExerciseAdapter adapter;
    GridLayoutManager gridLayoutManager;
    RoutineView routineView;
    List<Exercise> exercises;

    public CycleAdapter(List<Cycle> data, RoutineView routineView) {
        this.data = data;
        this.routineView = routineView;
    }

    @NonNull
    @Override
    public CycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.cycle_item, parent, false);
        return new CycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CycleViewHolder holder, int position) {
        Cycle current = data.get(position);

        holder.titCycle.setText(current.getName());
        holder.cycleReps.setText("x" + String.valueOf(current.getRepetitions()));

        RecyclerView listView = view.findViewById(R.id.listExcercises);

        FittyApp fittyApp = (FittyApp) routineView.getActivity().getApplication();
        fittyApp.getExerciseRepository().getRoutineCycleExercises(routineView.getRoutine().getId(), current.getId(), 0, 99, "id", "asc").observe(routineView.getActivity(),r->{
            if(r.getStatus()== Status.SUCCESS){
                assert r.getData() != null;
                exercises = r.getData().getResults();
                Log.d("CANTIDAD", String.valueOf(exercises.size()));
                current.addExercise(exercises);
                Log.d("CANTIDAD", String.valueOf(exercises.size()));

                adapter = new ExerciseAdapter(exercises);
                gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
                listView.setLayoutManager(gridLayoutManager);
                listView.setAdapter(adapter);;
                Log.d("CANTIDAD", String.valueOf(exercises.size()));

            }
            else
                defaultResourceHandler(r);
        });
    }

    private void defaultResourceHandler(Resource<?> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                Log.d("UI", routineView.getActivity().getString(R.string.loading));

                break;
            case ERROR:
                Error error = resource.getError();
                String message = routineView.getActivity().getString(R.string.error, error.getDescription(), error.getCode());
                Log.d("UI", message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CycleViewHolder extends RecyclerView.ViewHolder {
        TextView titCycle;
        TextView cycleReps;

        public CycleViewHolder(@NonNull View itemView) {
            super(itemView);
            titCycle = itemView.findViewById(R.id.cycleTit);
            cycleReps = itemView.findViewById(R.id.cycleReps);
        }
    }
}
