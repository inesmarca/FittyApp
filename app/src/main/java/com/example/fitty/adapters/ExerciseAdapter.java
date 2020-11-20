package com.example.fitty.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> data;
    private Cycle cycle;
    private RoutineView routineView;

    public ExerciseAdapter(List<Exercise> data, Cycle cycle, RoutineView routineView) {
        this.data = data;
        this.cycle = cycle;
        this.routineView = routineView;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise current = data.get(position);

        holder.exerciseTitle.setText(current.getName());
        if (current.getRepetitions() != 0) {
            holder.exerciseReps.setText("x" + String.valueOf(current.getRepetitions()));
        } else {
            holder.exerciseReps.setText(String.valueOf(current.getDuration()) + "s");
        }

        FittyApp fittyApp = (FittyApp) routineView.getActivity().getApplication();
        fittyApp.getVideosRepository().getRoutineCycleExerciseVideo(routineView.getRoutine().getId(), cycle.getId(), current.getId(), 0, 1, null, null).observe(routineView.getActivity(),r->{
            if(r.getStatus()== Status.SUCCESS){
                assert r.getData() != null;
                if (!r.getData().getResults().isEmpty()) {
                    current.setUrlVideo(r.getData().getResults().get(0));
                }
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

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseTitle;
        TextView exerciseReps;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.exerciseTit);
            exerciseReps = itemView.findViewById(R.id.exerciseReps);
        }
    }
}
