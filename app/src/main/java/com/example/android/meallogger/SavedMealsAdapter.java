package com.example.android.meallogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.meallogger.data.Meal;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedMealsAdapter extends RecyclerView.Adapter<SavedMealsAdapter.SavedMealsViewHolder> {
    private List<Meal> mSavedMealsList;
    private OnSavedMealClickListener mMealClickListener;

    interface OnSavedMealClickListener {
        void onSavedMealClicked(Meal meal);
    }

    public SavedMealsAdapter(OnSavedMealClickListener listener) {
        mMealClickListener = listener;
    }

    public void updateSavedMeals(List<Meal> savedMealsList) {
        mSavedMealsList = savedMealsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mSavedMealsList != null) {
            return mSavedMealsList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public SavedMealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.saved_meal, parent, false);
        return new SavedMealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedMealsViewHolder holder, int position) {
        holder.bind(mSavedMealsList.get(position));
    }

    class SavedMealsViewHolder extends RecyclerView.ViewHolder {
        private TextView mMealTitleTV;
        private TextView mMealTypeTV;
        private TextView mMealDateTV;
        private TextView mMealTimeTV;

        SavedMealsViewHolder(View itemView) {
            super(itemView);
            mMealTitleTV = itemView.findViewById(R.id.tv_meal_name);
            mMealTypeTV = itemView.findViewById(R.id.tv_meal_type);
            mMealDateTV = itemView.findViewById(R.id.tv_meal_date);
            mMealTimeTV = itemView.findViewById(R.id.tv_meal_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMealClickListener.onSavedMealClicked(mSavedMealsList.get(getAdapterPosition()));
                }
            });
        }

        void bind(Meal meal) {
            mMealTitleTV.setText(meal.title);
            mMealTypeTV.setText(meal.type);
            mMealDateTV.setText(meal.date);
            mMealTimeTV.setText(meal.time);
        }
    }

}
