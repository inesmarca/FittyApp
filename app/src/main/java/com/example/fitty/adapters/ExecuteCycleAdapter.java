package com.example.fitty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.ExecuteActivity;
import com.example.fitty.R;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExecuteCycleAdapter extends RecyclerView.Adapter<ExecuteCycleAdapter.ExecuteCycleViewHolder> {
    private List<Cycle> cycles;
    private List<ExecuteExerciseAdapter> adapters;
    ExecuteActivity activity;

    public ExecuteCycleAdapter(List<Cycle> cycles, ExecuteActivity activity) {
        this.cycles = cycles;
        this.activity = activity;
        adapters = new ArrayList<>();
        for (Cycle cycle: cycles) {
            adapters.add(new ExecuteExerciseAdapter(cycle.getExercises(), activity));
        }
    }


    public Exercise asignarEjecucion(int cycleIdx, int exerciseIdx) {
        if(cycleIdx < cycles.size()) {
            ExecuteExerciseAdapter adapter = adapters.get(cycleIdx);
            Exercise exercise = adapter.asignarEjecucion(exerciseIdx);
            adapter.notifyItemChanged(exerciseIdx);
            return exercise;
        }
        return null;
    }


    @NonNull
    @Override
    public ExecuteCycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.execute_cycle_item, parent, false);
        return new ExecuteCycleViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ExecuteCycleViewHolder holder, int position) {
        String cycleName = cycles.get(position).getName();
        holder.viewExecuteCycleName.setText(cycleName);

        ExecuteExerciseAdapter adapter = adapters.get(position);
        adapter.setParentRecyclerView(holder.recyclerExecuteExercises);
        holder.recyclerExecuteExercises.setLayoutManager(new LinearLayoutManager(activity));
        holder.recyclerExecuteExercises.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return cycles.size();
    }

    public class ExecuteCycleViewHolder extends RecyclerView.ViewHolder {

        TextView viewExecuteCycleName;
        RecyclerView recyclerExecuteExercises;

        public ExecuteCycleViewHolder(View itemView) {
            super(itemView);
            viewExecuteCycleName = itemView.findViewById(R.id.viewExecuteCycleName);
            recyclerExecuteExercises = itemView.findViewById(R.id.recyclerExecuteExercises);
        }
    }

}