package com.example.android.meallogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MealItemViewHolder> {
    // TODO Create data class for items
    class MealItemViewHolder extends RecyclerView.ViewHolder{

        public MealItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
    @NonNull
    @Override
    public MealItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.adapter_item,parent,false);
        return new MealItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MealItemViewHolder holder, int position) {
//        TODO getposition
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
