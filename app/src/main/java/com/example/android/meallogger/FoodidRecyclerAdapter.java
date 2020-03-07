package com.example.android.meallogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.FoodId;

import java.util.List;

public class FoodidRecyclerAdapter extends RecyclerView.Adapter<FoodidRecyclerAdapter.ResultViewHolder> {
    List<FoodId> mFoodChoices;

    TextView mTvAdapterDesc;
    TextView mTvAdapterDataType;
    TextView mTvAdapterBrandOwner;
    TextView mTvAdapterId;
    OnResultClickListener mClickListener;

    interface OnResultClickListener {
        void onRVClicked(FoodId food);
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

    public void clear() {
        int size = getItemCount();
        if(size>0){
            mFoodChoices.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAdapterDesc = itemView.findViewById(R.id.tv_adapter_choice_description);
            mTvAdapterDataType = itemView.findViewById(R.id.tv_adapter_choice_data_type);
            mTvAdapterBrandOwner = itemView.findViewById(R.id.tv_adapter_choice_brand_owner);
            mTvAdapterId = itemView.findViewById(R.id.tv_adapter_choice_id);
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
            mTvAdapterDesc.setText(foodId.description);
            mTvAdapterDataType.setText(foodId.dataType);
            mTvAdapterId.setText(String.valueOf(foodId.fdcId));
            if(foodId.dataType.equals("Branded")){
                mTvAdapterBrandOwner.setText(foodId.brandOwner);
            } else{
                mTvAdapterBrandOwner.setText("");
            }
        }
    }
}
