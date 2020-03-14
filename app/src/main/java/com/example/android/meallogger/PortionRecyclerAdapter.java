package com.example.android.meallogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.MealItem;
import com.example.android.meallogger.data.PortionDescription;

import java.util.List;

public class PortionRecyclerAdapter extends RecyclerView.Adapter<PortionRecyclerAdapter.ResultViewHolder> {
    private static final String TAG = PortionRecyclerAdapter.class.getSimpleName();

    List<PortionDescription> mPortionChoices;
    OnResultClickListener mClickListener;

    interface OnResultClickListener {
        void onItemPortion(PortionDescription portion);
    }

    public PortionRecyclerAdapter(OnResultClickListener listener){
        mClickListener = listener;
    }

    public void updateAdapter(List<PortionDescription> list){
        mPortionChoices = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PortionRecyclerAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_portion_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortionRecyclerAdapter.ResultViewHolder holder, int position) {
        holder.bind(mPortionChoices.get(position));
    }

    @Override
    public int getItemCount() {
        if (mPortionChoices != null){
            return mPortionChoices.size();
        } else {
            return 0;
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{
        TextView mPortionItemDesc;
        TextView mPortionItemModifier;
        TextView mPortionGramWeight;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mPortionItemDesc = itemView.findViewById(R.id.portion_item_desc);
            mPortionItemModifier = itemView.findViewById(R.id.portion_item_modifier);
            mPortionGramWeight = itemView.findViewById(R.id.portion_item_gram);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Set color
                    mClickListener.onItemPortion(mPortionChoices.get(getAdapterPosition()));
                    }
                });
            }

        public void bind(PortionDescription portionDescription) {
            mPortionItemDesc.setText(portionDescription.portionDescription);
            mPortionItemModifier.setText(portionDescription.modifier);
            mPortionGramWeight.setText(String.valueOf(portionDescription.gramWeight));
        }
    }
}
