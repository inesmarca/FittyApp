package com.example.fitty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.R;
import com.example.fitty.models.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> data;

    public ExerciseAdapter(List<Exercise> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise current = data.get(position);

        holder.exerciseTitle.setText(current.getName());
        if (current.getRepetitions() != 0) {
            holder.exerciseReps.setText(String.valueOf(current.getRepetitions()));
        } else {
            holder.exerciseReps.setText(String.valueOf(current.getDuration()));
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
