package com.example.fitty.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.ExecuteActivity;
import com.example.fitty.R;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Exercise;

import java.util.ArrayList;
import java.util.List;



public class ExecuteExerciseAdapter extends RecyclerView.Adapter<ExecuteExerciseAdapter.ExecuteExerciseViewHolder> {
    enum State {PENDING, RUNNING, FINISHED};
    private List<ExcerciseWithState> exercises;

    private ExecuteActivity activity;

    RecyclerView parentRecyclerView;

    public ExecuteExerciseAdapter(List<Exercise> exercises, ExecuteActivity activity) {
        this.exercises = new ArrayList<>();
        this.activity = activity;
        for (Exercise exercise : exercises) {
            this.exercises.add(new ExcerciseWithState(exercise, State.PENDING));
        }
    }

    public void setParentRecyclerView(RecyclerView parentRecyclerView) {
        this.parentRecyclerView = parentRecyclerView;
    }

    public Exercise asignarEjecucion(int exerciseIdx) {
        if(exerciseIdx < exercises.size()) {
            ExcerciseWithState excerciseWithState = exercises.get(exerciseIdx);
            excerciseWithState.state = State.RUNNING;
            exercises.set(exerciseIdx, excerciseWithState);
            return excerciseWithState.exercise;
        }
        return null;
    }


    @NonNull
    @Override
    public ExecuteExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.execute_exercise_item, parent, false);
        return new ExecuteExerciseViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ExecuteExerciseViewHolder holder, int position) {
        ExcerciseWithState excerciseWithState = exercises.get(position);
        String exerciseName = excerciseWithState.exercise.getName();
        holder.viewExecuteExerciseName.setText(exerciseName);

        //De acuerdo al estado cambia el fondo
        switch (excerciseWithState.state) {
            case PENDING:
                holder.linearLayoutExecuteExercise.setBackgroundResource(R.drawable.exercise_execution_border_with_bg);
                break;
            case RUNNING:
                holder.linearLayoutExecuteExercise.setBackgroundResource(R.drawable.exercise_execution_border_no_bg);
                if(excerciseWithState.exercise.getRepetitions() > 0)
                    holder.radioExerciseFinished.setVisibility(View.VISIBLE);
                    holder.radioExerciseFinished.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                    holder.radioExerciseFinished.setOnClickListener(view -> {
                        excerciseWithState.state = State.FINISHED;
                        holder.radioExerciseFinished.setImageResource(R.drawable.ic_baseline_check_24);
                        //notifyItemChanged(position);
                        activity.siguienteEjercicio();
                    });
                break;
            case FINISHED:
                holder.linearLayoutExecuteExercise.setBackgroundResource(R.drawable.exercise_execution_border_no_bg);
                holder.radioExerciseFinished.setVisibility(View.VISIBLE);
                holder.radioExerciseFinished.setImageResource(R.drawable.ic_baseline_check_24);
                break;
        }

        //Veamos si es por duracion o repeticiones
        if(excerciseWithState.exercise.getDuration() > 0) {
            holder.viewExecuteExerciseCant.setText(String.valueOf(excerciseWithState.exercise.getDuration()) + "s");
        } else {
            holder.viewExecuteExerciseCant.setText("x"+String.valueOf(excerciseWithState.exercise.getRepetitions()));
        }

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExecuteExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView viewExecuteExerciseCant, viewExecuteExerciseName;
        ImageButton radioExerciseFinished;
        LinearLayout linearLayoutExecuteExercise;


        public ExecuteExerciseViewHolder(View itemView) {
            super(itemView);
            viewExecuteExerciseCant = itemView.findViewById(R.id.viewExecuteExerciseCant);
            viewExecuteExerciseName = itemView.findViewById(R.id.viewExecuteExerciseName);
            radioExerciseFinished = itemView.findViewById(R.id.radioExerciseFinished);
            linearLayoutExecuteExercise = itemView.findViewById(R.id.linearLayoutExecuteExercise);
        }
    }

    private class ExcerciseWithState {
        private Exercise exercise;
        private State state;
        ExcerciseWithState(Exercise exercise, State state) {
            this.exercise = exercise;
            this.state = state;
        }
    }

}