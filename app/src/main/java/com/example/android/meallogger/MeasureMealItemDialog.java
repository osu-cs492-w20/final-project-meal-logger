package com.example.android.meallogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Measure;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.MealItem;
import com.example.android.meallogger.data.PortionDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MeasureMealItemDialog extends DialogFragment {
    MealItem mPortionItem;
    PortionDescription mSelected;
    int mSelectIndex;
    ArrayAdapter<String> mAdapter;
    TextView mTvSelected;
    EditText mEtAmount;

    AlertDialog.Builder mAdapterBuilder;


    private OnSubmitServingSize mAcceptListener;

    interface OnSubmitServingSize {
        void calculatePortions(int index, float amountOfServing);
    }


    private static final String TAG = MeasureMealItemDialog.class.getSimpleName();

    MeasureMealItemDialog(MealItem item, OnSubmitServingSize listener){ mPortionItem = item; mAcceptListener = listener; }

    @Override
    public void onStart() {
        super.onStart();

        mSelected = null;
        mEtAmount = getDialog().findViewById(R.id.dialog_amount_unit);
        mTvSelected = getDialog().findViewById(R.id.dialog_selected_unit);
        if(mPortionItem.appliedPortionIndex!=-1){
            mSelectIndex = mPortionItem.appliedPortionIndex;
            mSelected = mPortionItem.foodPortions.get(mPortionItem.appliedPortionIndex);
            mEtAmount.setText(String.valueOf(mPortionItem.amountPortion));
            mTvSelected.setText(mSelected.portionDescription);
        }
        mTvSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterBuilder = new AlertDialog.Builder(getDialog().getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                mAdapterBuilder.setView(inflater.inflate(R.layout.dialog_unit_list, null));
                mAdapterBuilder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selected) {
                        mSelectIndex = selected;
                        mSelected = mPortionItem.foodPortions.get(selected);
                        Log.d(TAG,"!==Selected: "+mSelected.portionDescription);
                        mTvSelected.setText(mSelected.portionDescription);
                    }
                });
                mAdapterBuilder.show();
            }
        });
//        mAddSaveEditText = getDialog().findViewById(R.id.location_add_et);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mAdapter = new ArrayAdapter<String>(getContext(), R.layout.adapter_portion_item);
        PortionDescription serving = null;

        // This dataTypes has no portion descriptions
        if(mPortionItem.dataType.equals("Branded")){
            PortionDescription createDesc = new PortionDescription();
            createDesc.dataType = "Branded";
            createDesc.portionDescription = mPortionItem.householdServingFullText;
            createDesc.gramWeight = mPortionItem.servingSize;
            mPortionItem.foodPortions.add(createDesc);
            mAdapter.add(createDesc.portionDescription);
        } else {
            for(int i=0; i< mPortionItem.foodPortions.size(); i++ ){
                serving = mPortionItem.foodPortions.get(i);

                // These dataTypes need their description to modifier string.
                if(serving.dataType.equals("Foundation")||serving.dataType.equals("SR Legacy")){
                    serving.portionDescription = serving.modifier;
                    mPortionItem.foodPortions.set(i, serving);
                }
                mAdapter.add(serving.portionDescription);
                Log.d(TAG, "!==Added "+serving.portionDescription);
            };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_portions, null))
            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(mSelected!=null && mEtAmount.getText().toString()!=""){
                            Log.d(TAG,"!=="+mSelected.portionDescription);
                            mAcceptListener.calculatePortions(mSelectIndex,
                                    Float.parseFloat(mEtAmount.getText().toString())
                            );
                        }
                    }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MeasureMealItemDialog.this.getDialog().cancel();
                }
            });
        return builder.create();
    }
}
