package com.example.android.meallogger.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealRepository;
import com.example.android.meallogger.data.Status;

import java.util.List;

public class MealCreationViewModel extends ViewModel {
    private MealRepository mRepository;
    private LiveData<Meal> mMeal;
    private LiveData<Status> mLoadingStatus;
    private LiveData<List<FoodId>> mFoodChoices;

    public MealCreationViewModel(){
        mRepository = new MealRepository();
        mMeal = mRepository.returnMeal();
        mLoadingStatus = mRepository.getStatus();
        mFoodChoices = mRepository.getChoices();
    }

    public void addItemToMeal(String query){
        mRepository.addFoodItemtoMeal(query);
    }

    public void getItemDetails(FoodId query){
        mRepository.lookupFoodDetails(query);
    }

    public LiveData<Meal> getMeal(){ return mMeal; }
    public LiveData<Status> getStatus(){ return mLoadingStatus; }
    public LiveData<List<FoodId>> getFoodChoices(){ return mFoodChoices; }
}
