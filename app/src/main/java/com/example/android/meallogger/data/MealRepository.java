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
                +"\nCalories:"+json.foodNutrients.get(2).amount+json.foodNutrients.get(2).nutrient.unitName
                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);

        Meal newValue;
        if(mFinalMeal.getValue()!=null){
            newValue = mFinalMeal.getValue();
        } else{
            newValue = new Meal();
        }
        newValue.items.add(0, json);

        // Sum of meal content
        newValue = addTotal(newValue, json.foodNutrients);

        mFinalMeal.setValue(newValue);
        mStatus.setValue(Status.SUCCESS);
    }

//    DecimalFormat df = new DecimalFormat("#####.###");
    private Meal addTotal(Meal total, List<FoodNutrient> list){
        for(int i=0; i<list.size(); i++){
            FoodNutrient ntr = list.get(i);
            switch(ntr.nutrient.name){
                case "Energy":
                    total.totalNutrients.calories.amount += ntr.amount;
                    break;
                case "Protein":
                    total.totalNutrients.protein.amount += ntr.amount;
                    break;
                case "Total lipid (fat)":
                    total.totalNutrients.fat.amount += ntr.amount;
                    break;
                case "Fatty acids, total saturated":
                    total.totalNutrients.saturatedFat.amount += ntr.amount;
                    break;
                case "Fatty acids, total trans":
                    total.totalNutrients.transFat.amount += ntr.amount;
                    break;
                case "Carbohydrate, by difference":
                    total.totalNutrients.carbohydrates.amount += ntr.amount;
                    break;
                case "Sugars, total including NLEA":
                    total.totalNutrients.sugars.amount += ntr.amount;
                    break;
                case "Iron, Fe":
                    total.totalNutrients.iron.amount += ntr.amount;
                    break;
                case "Sodium, Na":
                    total.totalNutrients.sodium.amount += ntr.amount;
                    break;
                case "Calcium, Ca":
                    total.totalNutrients.calcium.amount += ntr.amount;
                    break;
                case "Cholesterol":
                    total.totalNutrients.cholesterol.amount += ntr.amount;
                    break;
                default:
            }
        }
        return total;
    }
    private Meal subTotal(Meal total, List<FoodNutrient> list){
        for(int i=0; i<list.size(); i++){
            FoodNutrient ntr = list.get(i);
            switch(ntr.nutrient.name){
                case "Energy":
                    total.totalNutrients.calories.amount -= ntr.amount;
                    break;
                case "Protein":
                    total.totalNutrients.protein.amount -= ntr.amount;
                    break;
                case "Total lipid (fat)":
                    total.totalNutrients.fat.amount -= ntr.amount;
                    break;
                case "Fatty acids, total saturated":
                    total.totalNutrients.saturatedFat.amount -= ntr.amount;
                    break;
                case "Fatty acids, total trans":
                    total.totalNutrients.transFat.amount -= ntr.amount;
                    break;
                case "Carbohydrate, by difference":
                    total.totalNutrients.carbohydrates.amount -= ntr.amount;
                    break;
                case "Sugars, total including NLEA":
                    total.totalNutrients.sugars.amount -= ntr.amount;
                    break;
                case "Iron, Fe":
                    total.totalNutrients.iron.amount -= ntr.amount;
                    break;
                case "Sodium, Na":
                    total.totalNutrients.sodium.amount -= ntr.amount;
                    break;
                case "Calcium, Ca":
                    total.totalNutrients.calcium.amount -= ntr.amount;
                    break;
                case "Cholesterol":
                    total.totalNutrients.cholesterol.amount -= ntr.amount;
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
        Log.d(TAG, "!===Removing:"+index);

        MealItem removeItem = updateMeal.items.get(index);
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
}
