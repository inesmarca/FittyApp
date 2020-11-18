package com.example.fitty.adapters;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.FittyApp;
import com.example.fitty.R;
import com.example.fitty.api.models.Category;
import com.example.fitty.api.models.Routine;

import java.util.List;

public class BestAdapter extends RecyclerView.Adapter<BestAdapter.BestViewHolder> {

    private List<Routine> data;
    private OnRoutineListener mOnRoutineListener;
    private Application application;

    public BestAdapter(List<Routine> data, OnRoutineListener onRoutineListener, Application application) {
        this.data = data;
        this.application = application;
        this.mOnRoutineListener = onRoutineListener;
    }

    @NonNull
    @Override
    public BestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new BestViewHolder(view, mOnRoutineListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BestViewHolder holder, int position) {
        Routine current = data.get(position);
        FittyApp fittyApp = (FittyApp) application;
        holder.titRoutine.setText(current.getName());
        holder.category.setImageResource(current.getCategory().getIcon());
        holder.rating.setRating(current.getRating());
        holder.durationRoutine.setText(current.getDuration() + '\'');
        if (current.isFavorite()) {
            holder.favorite.setImageResource(R.drawable.ic_favorite_full);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnRoutineListener onRoutineListener;
        TextView titRoutine;
        RatingBar rating;
        TextView durationRoutine;
        ImageView category;
        ImageButton favorite;

        public BestViewHolder(@NonNull View itemView, OnRoutineListener onRoutineListener) {
            super(itemView);
            titRoutine = itemView.findViewById(R.id.titRoutine);
            rating = itemView.findViewById(R.id.ratingRoutine);
            durationRoutine = itemView.findViewById(R.id.durRoutine);
            category = itemView.findViewById(R.id.routineCat);
            favorite = itemView.findViewById(R.id.favorite);

            this.onRoutineListener = onRoutineListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRoutineListener.OnRoutineClick(data.get(getAdapterPosition()));
        }
    }

    public interface OnRoutineListener {
        void OnRoutineClick(Routine routine);
    }
}
