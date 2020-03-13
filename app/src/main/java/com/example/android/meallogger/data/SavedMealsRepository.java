package com.example.android.meallogger.data;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SavedMealsRepository {
    private SavedMealsDAO mSavedMealsDao;
    private MutableLiveData<Status> mLoadingStatus;


    public SavedMealsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSavedMealsDao = db.savedMealsDao();
        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    private static class InsertMealAsyncTask
            extends AsyncTask<MealData, Void, Void> {
        private SavedMealsDAO mAsyncTaskDao;

        InsertMealAsyncTask(SavedMealsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(MealData... meal) {
            mAsyncTaskDao.insert(meal[0]);
            return null;
        }
    }

    private static class DeleteMealAsyncTask
            extends AsyncTask<MealData, Void, Void> {
        private SavedMealsDAO mAsyncTaskDao;

        DeleteMealAsyncTask(SavedMealsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(MealData... meal) {
            mAsyncTaskDao.delete(meal[0]);
            return null;
        }
    }

    public void insertMeal(MealData repo) {
        new InsertMealAsyncTask(mSavedMealsDao).execute(repo);
    }

    public void deleteMeal(MealData repo) {
        new DeleteMealAsyncTask(mSavedMealsDao).execute(repo);
    }

    public LiveData<List<MealData>> getAllMeals() {
        return mSavedMealsDao.getAllMeals();
    }

    public LiveData<MealData> getMealByName(String name) {
        return mSavedMealsDao.getMealByName(name);
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}