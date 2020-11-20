package com.example.fitty.adapters;

import android.annotation.SuppressLint;
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

public class CategoryRoutinesAdapter extends RecyclerView.Adapter<CategoryRoutinesAdapter.CategoryRoutinesViewHolder> {

    private List<Routine> data;
    private OnCategoryRoutineListener mOnCategoryRoutineListener;

    public CategoryRoutinesAdapter(List<Routine> data, OnCategoryRoutineListener onCategoryRoutineListener) {
        this.data = data;
        this.mOnCategoryRoutineListener = onCategoryRoutineListener;
    }

    @NonNull
    @Override
    public CategoryRoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new CategoryRoutinesViewHolder(view, mOnCategoryRoutineListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryRoutinesViewHolder holder, int position) {
        Routine current = data.get(position);

        holder.titRoutine.setText(current.getName());
        holder.category.setImageResource(current.getCategory().getIcon());
        holder.rating.setRating(current.getRating());
        holder.durationRoutine.setText(current.getDuration() + '\'');
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CategoryRoutinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView titRoutine;
        RatingBar rating;
        TextView durationRoutine;
        ImageView category;
        OnCategoryRoutineListener onCategoryRoutineListener;

        public CategoryRoutinesViewHolder(@NonNull View itemView, OnCategoryRoutineListener onCategoryRoutineListener) {
            super(itemView);

            titRoutine = itemView.findViewById(R.id.titRoutine);
            rating = itemView.findViewById(R.id.ratingRoutine);
            durationRoutine = itemView.findViewById(R.id.durRoutine);
            category = itemView.findViewById(R.id.routineCat);

            this.onCategoryRoutineListener = onCategoryRoutineListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoryRoutineListener.OnCategoryRoutineClick(data.get(getAdapterPosition()));
        }
    }

    public interface OnCategoryRoutineListener {
        void OnCategoryRoutineClick(Routine routine);
    }
}
