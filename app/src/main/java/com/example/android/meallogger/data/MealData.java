package com.example.android.meallogger.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "meals")
public class MealData implements Serializable {
    @PrimaryKey
    @NonNull
    public String name;
    public String description;
    public byte[] photo;
    public String totalFat;
    public String totalSatFat;
    public String totalCholesterol;
    public String totalSodium;
    public String totalCarb;
    public String totalSugar;
    public String totalProtein;
    public String totalCalcium;
    public String totalIron;
    public String totalCalories;
    public String foodsList;
    public String date;
    public String time;
    public String type;
}