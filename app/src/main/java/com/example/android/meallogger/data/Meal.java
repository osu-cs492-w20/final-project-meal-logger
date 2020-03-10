package com.example.android.meallogger.data;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    public List<MealItem> items;
    public MealNutrients totalNutrients;

    Meal(){
        items = new ArrayList<MealItem>();
        totalNutrients = new MealNutrients();
    }
}
