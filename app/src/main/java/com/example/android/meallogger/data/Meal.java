package com.example.android.meallogger.data;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    public String title;
    public List<MealItem> items;
    public MealNutrients totalNutrients;

    Meal(){
        title = new String();
        items = new ArrayList<MealItem>();
        totalNutrients = new MealNutrients();
    }
}
