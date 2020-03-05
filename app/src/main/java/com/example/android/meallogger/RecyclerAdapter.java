package com.example.android.meallogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.FoodId;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ResultViewHolder> {
    List<FoodId> mFoodChoices;

    TextView mAdapterTV;
    OnResultClickListener mClickListener;

    interface OnResultClickListener {
        void onRVClicked(FoodId food);
    }

    public RecyclerAdapter(OnResultClickListener listener){
        mClickListener = listener;
    }

    public void updateAdapter(List<FoodId> list){
        mFoodChoices = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ResultViewHolder holder, int position) {
        holder.bind(mFoodChoices.get(position));
    }

    @Override
    public int getItemCount() {
        if (mFoodChoices != null){
            return mFoodChoices.size();
        } else {
            return 0;
        }
    }


    class ResultViewHolder extends RecyclerView.ViewHolder{

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mAdapterTV = itemView.findViewById(R.id.tv_adapter_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onRVClicked(
                            mFoodChoices.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(FoodId foodId) {
            mAdapterTV.setText(foodId.description);
        }
    }
}
