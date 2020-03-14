package com.example.android.meallogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Measure;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.PortionDescription;

import java.util.List;

public class MeasureMealItemDialog extends DialogFragment implements PortionRecyclerAdapter.OnResultClickListener {
    RecyclerView mRvPortions;
    PortionRecyclerAdapter mAdapter;
    List<PortionDescription> mPortions;
    PortionDescription mSelected;

    private static final String TAG = MeasureMealItemDialog.class.getSimpleName();

    MeasureMealItemDialog(List<PortionDescription> item){ mPortions = item; }

    @Override
    public void onStart() {
        super.onStart();
        mSelected = null;
        mRvPortions = getDialog().findViewById(R.id.rv_portion_choices);
        mRvPortions.setLayoutManager(new LinearLayoutManager(getDialog().getContext()));
        mAdapter = new PortionRecyclerAdapter(this);
        mRvPortions.setAdapter(mAdapter);
        mAdapter.updateAdapter(mPortions);
//        mAddSaveEditText = getDialog().findViewById(R.id.location_add_et);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_portions, null))
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(mSelected!=null){
                            Log.d(TAG,"!=="+mSelected.portionDescription);
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

    @Override
    public void onItemPortion(PortionDescription portion) {
        mSelected = portion;
    }
}
