package com.example.android.meallogger.data;

import android.os.AsyncTask;

import com.example.android.meallogger.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;

public class APIQueryTask extends AsyncTask<String, Void, String> {
    private Callback mCallbackFC;
    private static final String TAG = APIQueryTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {
        String apiURL = params[0];
        String resultsJSON = null;
        try {
            resultsJSON = NetworkUtils.doHTTPGet(apiURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultsJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<FoodId> foodIds = null;
//        return s;
    }
}
