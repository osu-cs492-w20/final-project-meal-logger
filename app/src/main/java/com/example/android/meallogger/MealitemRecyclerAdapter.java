package com.example.android.meallogger;

import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MealitemRecyclerAdapter extends RecyclerView.Adapter<MealitemRecyclerAdapter.ResultViewHolder> {
    List<MealItem> mItems;
    static OnResultClickListener mClickListener;

    interface OnResultClickListener {
        void onItemSelected(MealItem item, int index);
    }

    public MealitemRecyclerAdapter(OnResultClickListener listener){
        mItems = new ArrayList<MealItem>();
        mClickListener = listener;
    }

    public void updateAdapter(List<MealItem> list){
        mItems = list;
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void insertAdapter(MealItem item){
        mItems.add(0, item);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public MealitemRecyclerAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_meal_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealitemRecyclerAdapter.ResultViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItems != null){
            return mItems.size();
        } else {
            return 0;
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{
        TextView mTvAdapterDesc;
        TextView mTvAdapterAmount;
        TextView mTvAdapterUnit;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAdapterDesc = itemView.findViewById(R.id.tv_adapter_item_description);
            mTvAdapterAmount = itemView.findViewById(R.id.tv_adapter_item_unit_amount);
            mTvAdapterUnit = itemView.findViewById(R.id.tv_adapter_item_unit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemSelected(
                            mItems.get(getAdapterPosition()), getAdapterPosition()
                    );
                }
            });
        }

        public void bind(MealItem item) {
            mTvAdapterDesc.setText(item.description);
            mTvAdapterAmount.setText(String.valueOf(item.amountPortion));
            if(item.appliedPortionIndex!=-1){
                mTvAdapterUnit.setText(item.foodPortions.get(item.appliedPortionIndex).portionDescription);
            } else {
                mTvAdapterUnit.setText("Tap to modify...");
            }
        }

        public void remove() {
            int position  = getAdapterPosition();
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }
}
