package com.example.android.meallogger.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;

import java.util.List;

@Dao
public interface SavedMealsDAO {
    @Insert
    void insert(MealData meal);

    @Delete
    void delete(MealData meal);

    @Query("SELECT * FROM meals")
    LiveData<List<MealData>> getAllMeals();

    @Query("SELECT * FROM meals WHERE name = :name LIMIT 1")
    LiveData<MealData> getMealByName(String name);
}