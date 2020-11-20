package com.example.fitty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.R;
import com.example.fitty.models.Routine;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Routine> data;
    private OnFavoriteListener mOnFavoriteListener;

    public FavoriteAdapter(List<Routine> data, OnFavoriteListener onFavoriteListener) {
        this.data = data;
        this.mOnFavoriteListener = onFavoriteListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new FavoriteViewHolder(view, mOnFavoriteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Routine current = data.get(position);
        current.setFavorite(true);
        holder.titRoutine.setText(current.getName());
        holder.category.setImageResource(current.getCategory().getIcon());
        holder.rating.setRating(current.getRating());
        holder.durationRoutine.setText(current.getDuration());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titRoutine;
        RatingBar rating;
        TextView durationRoutine;
        ImageView category;

        OnFavoriteListener onFavoriteListener;

        public FavoriteViewHolder(@NonNull View itemView, OnFavoriteListener onFavoriteListener) {
            super(itemView);

            titRoutine = itemView.findViewById(R.id.titRoutine);
            rating = itemView.findViewById(R.id.ratingRoutine);
            durationRoutine = itemView.findViewById(R.id.durRoutine);
            category = itemView.findViewById(R.id.routineCat);

            this.onFavoriteListener = onFavoriteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFavoriteListener.OnRoutineClick(data.get(getAdapterPosition()));
        }
    }

    public interface OnFavoriteListener {
        void OnRoutineClick(Routine routine);
    }
}
