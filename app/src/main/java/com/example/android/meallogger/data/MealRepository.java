package com.example.android.meallogger.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.meallogger.utils.UsdaAPIUtils;

import java.util.ArrayList;
import java.util.List;

public class MealRepository implements APIQueryTask.Callback{

    private static final String TAG = MealRepository.class.getSimpleName();
    private MutableLiveData<Meal> mFinalMeal;
    private MutableLiveData<Status> mStatus;
    private MutableLiveData<List<FoodId>> mFoodChoice;

    // Implement get status and get meal
    // Implement callback in createMealActivity to hide and show module

    public MealRepository(){
        mFinalMeal = new MutableLiveData<>();
        mFinalMeal.setValue(null);

        mStatus = new MutableLiveData<>();
        mStatus.setValue(Status.SUCCESS);

        mFoodChoice = new MutableLiveData<>();
        mFoodChoice.setValue(null);
    }
    public LiveData<Meal> returnMeal(){ return mFinalMeal; }
    public LiveData<Status> getStatus(){
        return mStatus;
    }
    public LiveData<List<FoodId>> getChoices(){ return mFoodChoice; }
    // 1/2 CallBack function for APIQueryTask
    // Gets ID, fetches food from ID
    public void handleSearchResults(ArrayList<FoodId> json){
        if(json.isEmpty()){
            Log.d(TAG, "!===Search Failed, No Results");
            mStatus.setValue(Status.ERROR);
            return;
        }
        Log.d(TAG, "!===Search Complete, Food Choices:"+json);
        mFoodChoice.setValue(json);
        mStatus.setValue(Status.DONE);
    }
    // 2/2 CallBack function for APIQueryTask
    // Get food details
    public void handleDetailResults(MealItem json){
        mStatus.setValue(Status.SUCCESS);
        Log.d(TAG, "!===Food:"+json.description
                +"\nCalories:"+json.labelNutrients.calories.value
                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);
    }

    public void addFoodItemtoMeal(String query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodSearchURL(query);
            Log.d(TAG, "!===Searching: "+query+"\n"+url);
            mStatus.setValue(Status.LOADING);
            new APIQueryTask(this).execute(url, "search");
        } else{
            mStatus.setValue(Status.ERROR);
            Log.d(TAG, "!===Failed addFoodItem(): No Query");
        }
    }

    public void lookupFoodDetails(FoodId query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodDetailsURL(query.fdcId);
            Log.d(TAG, "!===Querying:"+query.fdcId+"\n"+url);
            new APIQueryTask(this).execute(url, "detail");        }
    }
}
