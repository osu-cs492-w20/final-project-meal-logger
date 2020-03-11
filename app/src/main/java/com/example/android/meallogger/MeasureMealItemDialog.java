package com.example.android.meallogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MeasureMealItemDialog extends DialogFragment {
    EditText mAddSaveEditText;
    SharedPreferences mPreferences;


    @Override
    public void onStart() {
        super.onStart();
//        mAddSaveEditText = getDialog().findViewById(R.id.location_add_et);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

//        builder.setView(inflater.inflate(R.layout.dialog_add, null))
//                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        String newLocation = mAddSaveEditText.getText().toString();
//                        if (newLocation.length()>0){
//                            SharedPreferences.Editor edit = mPreferences.edit();
//                            edit.putString("pref_location", newLocation);
//                            edit.apply();
//                        }
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        AddLocationDialog.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }
}
