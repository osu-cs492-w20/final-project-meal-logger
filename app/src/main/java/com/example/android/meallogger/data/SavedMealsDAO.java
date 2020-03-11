package com.example.android.meallogger.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface SavedMealsDAO {
    @Insert
    void insert(MealData meal);

    @Delete
    void delete(MealData meal);
}