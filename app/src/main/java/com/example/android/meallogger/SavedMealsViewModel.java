package com.example.android.meallogger;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealData;
import com.example.android.meallogger.data.SavedMealsRepository;
import com.example.android.meallogger.data.Status;

import java.util.List;

public class SavedMealsViewModel extends AndroidViewModel {
    private SavedMealsRepository mSavedMealsRepository;
    private LiveData<List<Meal>> mSavedMeals;
    private LiveData<Status> mLoadingStatus;

    public SavedMealsViewModel(Application application) {
        super(application);
        mSavedMealsRepository =
                new SavedMealsRepository(application);
    }

    public void insertMeal(MealData meal) {
        mSavedMealsRepository.insertMeal(meal);
    }

    public void deleteSavedMeal(MealData meal) {
        mSavedMealsRepository.deleteMeal(meal);
    }

    public LiveData<List<Meal>> getSavedMeals() {
        return mSavedMeals;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}