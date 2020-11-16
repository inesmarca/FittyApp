package com.example.fitty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.R;
import com.example.fitty.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Category> data;
    private OnCategoryListener mOnCategoryListener;

    public CategoriesAdapter(List<Category> data, OnCategoryListener onCategoryListener) {
        this.data = data;
        this.mOnCategoryListener = onCategoryListener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_card, parent, false);
        return new CategoriesViewHolder(view, mOnCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category current = data.get(position);

        holder.imageCat.setImageResource(current.getIcon());
        holder.titCat.setText(current.getName());
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titCat;
        ImageView imageCat;
        OnCategoryListener onCategoryListener;

        public CategoriesViewHolder(@NonNull View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);

            titCat = itemView.findViewById(R.id.titCat);
            imageCat = itemView.findViewById(R.id.imgCat);

            this.onCategoryListener = onCategoryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            onCategoryListener.OnCategoryClick(data.get(getAdapterPosition()).getId());
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public interface OnCategoryListener {
        void OnCategoryClick(int id);
    }
}
