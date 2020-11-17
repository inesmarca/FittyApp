package com.example.fitty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.R;
import com.example.fitty.models.Cycle;

import java.util.List;

public class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.CycleViewHolder> {

    private List<Cycle> data;

    public CycleAdapter(List<Cycle> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cycle_item, parent, false);
        return new CycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CycleViewHolder holder, int position) {
        Cycle current = data.get(position);

        holder.titCycle.setText(current.getName());
        holder.cycleReps.setText(String.valueOf(current.getRepetitions()));
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
