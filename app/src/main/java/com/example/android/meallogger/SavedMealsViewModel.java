package com.example.android.meallogger;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.android.meallogger.data.MealData;
import com.example.android.meallogger.data.SavedMealsRepository;

public class SavedMealsViewModel extends AndroidViewModel {
    private SavedMealsRepository mSavedReposRepository;

    public SavedMealsViewModel(Application application) {
        super(application);
        mSavedReposRepository =
                new SavedMealsRepository(application);
    }

    public void insertMeal(MealData meal) {
        mSavedReposRepository.insertMeal(meal);
    }

    public void deleteGitHubRepo(MealData meal) {
        mSavedReposRepository.deleteMeal(meal);
    }
}