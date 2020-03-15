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
        json.appliedPortionIndex = -1;
        newValue.items.add(0, json);
        newValue.items.get(0).totalGramWeight = sumWeight(newValue.items.get(0).foodNutrients);
        // Sum of meal content
//        newValue = addTotal(newValue, json.foodNutrients);

        mFinalMeal.setValue(newValue);
        mStatus.setValue(Status.SUCCESS);
    }

    public Meal addTotal(Meal total, MealItem item){
        float multiplier = item.servingMultiplier;
        for(int i=0; i<item.foodNutrients.size(); i++){
            FoodNutrient ntr = item.foodNutrients.get(i);
            int safety;
            ntr.amount = ((float) ((int)(ntr.amount * 1000)) )/1000;
            switch(ntr.nutrient.name){
                case "Energy":
                    safety = (int)(total.totalNutrients.calories.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.calories.amount = (float)safety/1000;
                    break;
                case "Protein":
                    safety = (int)(total.totalNutrients.protein.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.protein.amount = (float)safety/1000;
                    break;
                case "Total lipid (fat)":
                    safety = (int)(total.totalNutrients.fat.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.fat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total saturated":
                    safety = (int)(total.totalNutrients.saturatedFat.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.saturatedFat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total trans":
                    safety = (int)(total.totalNutrients.transFat.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.transFat.amount = (float)safety/1000;
                    break;
                case "Carbohydrate, by difference":
                    safety = (int)(total.totalNutrients.carbohydrates.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.carbohydrates.amount = (float)safety/1000;
                    break;
                case "Sugars, total including NLEA":
                    safety = (int)(total.totalNutrients.sugars.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.sugars.amount = (float)safety/1000;
                    break;
                case "Iron, Fe":
                    safety = (int)(total.totalNutrients.iron.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.iron.amount = (float)safety/1000;
                    break;
                case "Sodium, Na":
                    safety = (int)(total.totalNutrients.sodium.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.sodium.amount = (float)safety/1000;
                    break;
                case "Calcium, Ca":
                    safety = (int)(total.totalNutrients.calcium.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.calcium.amount = (float)safety/1000;
                    break;
                case "Cholesterol":
                    safety = (int)(total.totalNutrients.cholesterol.amount*1000) + (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.cholesterol.amount = (float)safety/1000;
                    break;
                default:
            }
        }
        return total;
    }
    public Meal subTotal(Meal total, MealItem item){
        float multiplier = item.servingMultiplier;
        for(int i=0; i<item.foodNutrients.size(); i++){
            FoodNutrient ntr = item.foodNutrients.get(i);
            int safety;
            switch(ntr.nutrient.name){
                case "Energy":
                    safety = (int)(total.totalNutrients.calories.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.calories.amount = (float)safety/1000;
                    break;
                case "Protein":
                    safety = (int)(total.totalNutrients.protein.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.protein.amount = (float)safety/1000;
                    break;
                case "Total lipid (fat)":
                    safety = (int)(total.totalNutrients.fat.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.fat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total saturated":
                    safety = (int)(total.totalNutrients.saturatedFat.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.saturatedFat.amount = (float)safety/1000;
                    break;
                case "Fatty acids, total trans":
                    safety = (int)(total.totalNutrients.transFat.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.transFat.amount = (float)safety/1000;
                    break;
                case "Carbohydrate, by difference":
                    safety = (int)(total.totalNutrients.carbohydrates.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.carbohydrates.amount = (float)safety/1000;
                    break;
                case "Sugars, total including NLEA":
                    safety = (int)(total.totalNutrients.sugars.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.sugars.amount = (float)safety/1000;
                    break;
                case "Iron, Fe":
                    safety = (int)(total.totalNutrients.iron.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.iron.amount = (float)safety/1000;
                    break;
                case "Sodium, Na":
                    safety = (int)(total.totalNutrients.sodium.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.sodium.amount = (float)safety/1000;
                    break;
                case "Calcium, Ca":
                    safety = (int)(total.totalNutrients.calcium.amount*1000) - (int)(ntr.amount * multiplier * 1000);
                    total.totalNutrients.calcium.amount = (float)safety/1000;
                    break;
                case "Cholesterol":
                    safety = (int)(total.totalNutrients.cholesterol.amount*1000) - (int)(ntr.amount * multiplier * 1000);
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

        MealItem removeItem = updateMeal.items.get(index);
        Log.d(TAG, "!===Removing:"+removeItem.description);

        mFinalMeal.setValue(subTotal(updateMeal, removeItem));
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

    public void updatePortionMult(int fIndex, int pIndex, float amountOfServing){
        Meal newValue = mFinalMeal.getValue();
        MealItem editItem = newValue.items.get(fIndex);
        Log.d(TAG, "!==pIndex:"+pIndex+"=="+editItem.appliedPortionIndex);
        if(editItem.appliedPortionIndex!=pIndex || editItem.amountPortion!=amountOfServing){
            if(editItem.appliedPortionIndex != -1){
                newValue = subTotal(newValue, newValue.items.get(fIndex));
            }
            editItem.appliedPortionIndex = pIndex;
            editItem.amountPortion = amountOfServing;
            editItem.servingMultiplier = (editItem.foodPortions.get(pIndex).gramWeight * amountOfServing) / editItem.totalGramWeight;
            editItem.servingMultiplier = (float)((int)(editItem.servingMultiplier*1000))/1000;

            newValue.items.set(fIndex, editItem);
            newValue = addTotal(newValue, newValue.items.get(fIndex));
            mFinalMeal.setValue(newValue);
        }

    }

    private float sumWeight(List<FoodNutrient> nutrients){
        float sum = 0;
        for(FoodNutrient n : nutrients){
            switch(n.nutrient.unitName){
                case "g":
                    sum+=n.amount;
                case "mg":
                    sum+=n.amount*0.001;
            }
        }
        sum = (float)(((int)(sum * 1000)))/1000;
        return sum;
    }
}
