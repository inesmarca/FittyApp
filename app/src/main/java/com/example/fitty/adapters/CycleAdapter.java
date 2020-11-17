package com.example.fitty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.R;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.CycleViewHolder> {

       /*Para llamar a la api laburamos con los repositorios. Hagan un getApplication y casteenlo
   a FittyApp, de ahi agarran el repo que necesiten y usan los métodos. Saludos :) */

    //Como aca estas queriendo hacerlo desde un adapter, para el getApplication vas a tener q hacer cositas

    private List<Cycle> data;
    View view;
    List<Exercise> exercises;
    ExerciseAdapter adapter;
    GridLayoutManager gridLayoutManager;

    public CycleAdapter(List<Cycle> data) {
        this.data = data;
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

        exercises = new ArrayList<>();
        exercises.add(new Exercise(0, "Jumping Jacks", "Calentamiento", "warmup", 1, 30, 1));
        exercises.add(new Exercise(1, "Abdominales", "Calentamiento", "warmup", 1, 30, 2));
        exercises.add(new Exercise(2, "Push Ups", "Calentamiento", "warmup", 1, 30, 3));

        adapter = new ExerciseAdapter(exercises);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(adapter);
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
