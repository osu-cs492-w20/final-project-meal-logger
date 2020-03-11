package com.example.android.meallogger.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.android.meallogger.utils.UsdaAPIUtils;


public class MealRepository implements APIQueryTask.Callback{

    private static final String TAG = MealRepository.class.getSimpleName();
    private MutableLiveData<Meal> mFinalMeal;
    private MutableLiveData<Status> mStatus;
    private MutableLiveData<ArrayList<FoodId>> mFoodChoice;


    public MealRepository(){
        mFinalMeal = new MutableLiveData<>();
        mFinalMeal.setValue(new Meal());

        mStatus = new MutableLiveData<>();
        mStatus.setValue(Status.ERROR);

        mFoodChoice = new MutableLiveData<>();
        mFoodChoice.setValue(null);
    }

    public LiveData<Meal> returnMeal(){ return mFinalMeal; }
    public LiveData<Status> getStatus(){
        return mStatus;
    }
    public MutableLiveData<ArrayList<FoodId>> getChoices(){ return mFoodChoice; }

    // 1/2 CallBack function for APIQueryTask
    // Gets ID, fetches food from ID
    public void handleSearchResults(ArrayList<FoodId> json){
        if(json.isEmpty()){
            Log.d(TAG, "!===Search Failed, No Results");
            mStatus.setValue(Status.ERROR);
            return;
        }
        mFoodChoice.setValue(json);
        mStatus.setValue(Status.DONE);
    }
    // 2/2 CallBack function for APIQueryTask
    // Get food details
    public void handleDetailResults(MealItem json){
        mFoodChoice.setValue(null);
        Log.d(TAG, "!===Food:"+json.description
                +"\nCalories:"+json.foodNutrients.get(3).amount+json.foodNutrients.get(2).nutrient.unitName
                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);

        Meal newValue;
        newValue = mFinalMeal.getValue();
        newValue.items.add(0, json);

        // Sum of meal content
        newValue = addTotal(newValue, json.foodNutrients);

        mFinalMeal.setValue(newValue);
        mStatus.setValue(Status.SUCCESS);
    }

    private Meal addTotal(Meal total, List<FoodNutrient> list){
        for(int i=0; i<list.size(); i++){
            FoodNutrient ntr = list.get(i);
            int safety;
            ntr.amount = ((float) ((int)(ntr.amount * 1000)) )/1000;
            switch(ntr.nutrient.name){
                case "Energy":
                    safety = (int)(total.totalNutrients.calories.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.calories.amount = (float)safety/1000;
                    break;
                case "Protein":
                    safety = (int)(total.totalNutrients.protein.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.protein.amount = (float)safety/1000;
                    break;
                case "Total lipid (fat)":
                    safety = (int)(total.totalNutrients.fat.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.fat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total saturated":
                    safety = (int)(total.totalNutrients.saturatedFat.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.saturatedFat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total trans":
                    safety = (int)(total.totalNutrients.transFat.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.transFat.amount = (float)safety/1000;
                    break;
                case "Carbohydrate, by difference":
                    safety = (int)(total.totalNutrients.carbohydrates.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.carbohydrates.amount = (float)safety/1000;
                    break;
                case "Sugars, total including NLEA":
                    safety = (int)(total.totalNutrients.sugars.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.sugars.amount = (float)safety/1000;
                    break;
                case "Iron, Fe":
                    safety = (int)(total.totalNutrients.iron.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.iron.amount = (float)safety/1000;
                    break;
                case "Sodium, Na":
                    safety = (int)(total.totalNutrients.sodium.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.sodium.amount = (float)safety/1000;
                    break;
                case "Calcium, Ca":
                    safety = (int)(total.totalNutrients.calcium.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.calcium.amount = (float)safety/1000;
                    break;
                case "Cholesterol":
                    safety = (int)(total.totalNutrients.cholesterol.amount*1000) + (int)(ntr.amount * 1000);
                    total.totalNutrients.cholesterol.amount = (float)safety/1000;
                    break;
                default:
            }
        }
        return total;
    }
    private Meal subTotal(Meal total, List<FoodNutrient> list){
        for(int i=0; i<list.size(); i++){
            FoodNutrient ntr = list.get(i);
            int safety;
            switch(ntr.nutrient.name){
                case "Energy":
                    safety = (int)(total.totalNutrients.calories.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.calories.amount = (float)safety/1000;
                    break;
                case "Protein":
                    safety = (int)(total.totalNutrients.protein.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.protein.amount = (float)safety/1000;
                    break;
                case "Total lipid (fat)":
                    safety = (int)(total.totalNutrients.fat.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.fat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total saturated":
                    safety = (int)(total.totalNutrients.saturatedFat.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.saturatedFat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total trans":
                    safety = (int)(total.totalNutrients.transFat.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.transFat.amount = (float)safety/1000;
                    break;
                case "Carbohydrate, by difference":
                    safety = (int)(total.totalNutrients.carbohydrates.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.carbohydrates.amount = (float)safety/1000;
                    break;
                case "Sugars, total including NLEA":
                    safety = (int)(total.totalNutrients.sugars.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.sugars.amount = (float)safety/1000;
                    break;
                case "Iron, Fe":
                    safety = (int)(total.totalNutrients.iron.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.iron.amount = (float)safety/1000;
                    break;
                case "Sodium, Na":
                    safety = (int)(total.totalNutrients.sodium.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.sodium.amount = (float)safety/1000;
                    break;
                case "Calcium, Ca":
                    safety = (int)(total.totalNutrients.calcium.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.calcium.amount = (float)safety/1000;
                    break;
                case "Cholesterol":
                    safety = (int)(total.totalNutrients.cholesterol.amount*1000) - (int)(ntr.amount * 1000);
                    total.totalNutrients.cholesterol.amount = (float)safety/1000;
                    break;
                default:
            }
        }
        return total;
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

    public void removeFoodItemfromMeal(int index){
        Meal updateMeal = mFinalMeal.getValue();

//        for(int r=0; r < updateMeal.items.size(); r++){

        MealItem removeItem = updateMeal.items.get(index);
        Log.d(TAG, "!===Removing:"+removeItem.description);

//            if(removeItem.fdcId==query){
//                updateMeal.items.remove(r);
        mFinalMeal.setValue(subTotal(updateMeal, removeItem.foodNutrients));
//                return;
//            }
//        }
    }

    public void lookupFoodDetails(FoodId query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodDetailsURL(query.fdcId);
            Log.d(TAG, "!===Querying:"+query.fdcId+"\n"+url);
            new APIQueryTask(this).execute(url, "detail");
        }
    }

    public void updateName(String rename){
        Meal newValue = mFinalMeal.getValue();
        newValue.title = rename;
        mFinalMeal.setValue(newValue);
    }
}
