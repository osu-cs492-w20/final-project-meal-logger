package com.example.android.meallogger.data;

import android.app.Application;
import android.os.AsyncTask;

public class SavedMealsRepository {
    private SavedMealsDAO mSavedMealsDao;

    public SavedMealsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSavedMealsDao = db.savedMealsDao();
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
}