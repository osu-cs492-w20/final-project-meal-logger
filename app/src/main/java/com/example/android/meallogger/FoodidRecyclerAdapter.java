package com.example.android.meallogger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.FoodId;

import java.util.List;

public class FoodidRecyclerAdapter extends RecyclerView.Adapter<FoodidRecyclerAdapter.ResultViewHolder> {
    private static final String TAG = FoodidRecyclerAdapter.class.getSimpleName();

    List<FoodId> mFoodChoices;
    OnResultClickListener mClickListener;

    interface OnResultClickListener {
        void onChoiceClicked(FoodId food);
    }

    public FoodidRecyclerAdapter(OnResultClickListener listener){
        mClickListener = listener;
    }

    public void updateAdapter(List<FoodId> list){
        mFoodChoices = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodidRecyclerAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_choice_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodidRecyclerAdapter.ResultViewHolder holder, int position) {
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
        TextView mTvAdapterDesc;
        TextView mTvAdapterDataType;
        TextView mTvAdapterBrandOwner;
        TextView mTvAdapterId;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAdapterDesc = itemView.findViewById(R.id.tv_adapter_choice_description);
            mTvAdapterDataType = itemView.findViewById(R.id.tv_adapter_choice_data_type);
            mTvAdapterBrandOwner = itemView.findViewById(R.id.tv_adapter_choice_brand_owner);
            mTvAdapterId = itemView.findViewById(R.id.tv_adapter_choice_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onChoiceClicked(
                            mFoodChoices.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(FoodId foodId) {
            mTvAdapterDesc.setText(foodId.description);
            mTvAdapterDataType.setText(foodId.dataType);
            mTvAdapterId.setText(String.valueOf(foodId.fdcId));
            switch(foodId.dataType){
                case "Branded":
                    mTvAdapterBrandOwner.setText(foodId.brandOwner);
                    break;
                default:
                    mTvAdapterBrandOwner.setText("");
            }
        }
    }
}
