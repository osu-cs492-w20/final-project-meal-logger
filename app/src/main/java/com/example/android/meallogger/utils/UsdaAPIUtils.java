package com.example.android.meallogger.utils;

import android.net.Uri;

import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.MealItem;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UsdaAPIUtils {

    public static final String USDA_BASE_URL = "https://api.nal.usda.gov/fdc/v1/";
    public static final String USDA_API_KEY = "J8vThKbo9oxo6LktY1AEVXOTSvWRLMbj3gkjM1oV";

    static class IdLookupResults {
        ArrayList<FoodId> foods;
    }

// buildUsdaURL
// https://api.nal.usda.gov/fdc/v1/search?api_key=J8vThKbo9oxo6LktY1AEVXOTSvWRLMbj3gkjM1oV&generalSearchInput=Peanut Butter&requireAllWords=true
// searchType will either replace  ^^^^^^ "search" with parameter generalSearchInput="query" ^^^^^^^^^^^^^^^^^^^^^^^^^
    public static String buildFoodSearchURL(String query){
        return Uri.parse(USDA_BASE_URL+"search").buildUpon()
                .appendQueryParameter("api_key", USDA_API_KEY)
                .appendQueryParameter("generalSearchInput", query)
                .appendQueryParameter("requireAllWords", "true")
                .build()
                .toString();
    }

// https://api.nal.usda.gov/fdc/v1/339463?api_key=J8vThKbo9oxo6LktY1AEVXOTSvWRLMbj3gkjM1oV
//                                 ^^^^^^ replace "search" with "searchType": a usda Id for a food (usdaID field in FoodID).

    public static String buildFoodDetailsURL(int query){
        return Uri.parse(USDA_BASE_URL+String.valueOf(query)).buildUpon()
                .appendQueryParameter("api_key", USDA_API_KEY)
                .build()
                .toString();
    }

    public static ArrayList<FoodId> parseSearchJSON(String searchJSON){
        Gson gson = new Gson();
        IdLookupResults foodIDS = gson.fromJson(searchJSON, IdLookupResults.class);
        if (foodIDS != null && foodIDS.foods != null){
            return foodIDS.foods;
        } else{
            return null;
        }
    }
    public static MealItem parseDetailJSON(String detailJSON){
        Gson gson = new Gson();
        MealItem details = gson.fromJson(detailJSON, MealItem.class);
        if (details != null && details.foodNutrients != null){
            return details;
        } else {
            return null;
        }

    }

}
