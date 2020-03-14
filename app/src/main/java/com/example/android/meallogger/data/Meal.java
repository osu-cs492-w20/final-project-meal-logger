package com.example.android.meallogger.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {
    public String title;
    public String type;
    public String date;
    public String time;
    public List<MealItem> items;
    public MealNutrients totalNutrients;

    Meal(){
        title = new String();
        type = new String();
        date = new String();
        time = new String();
        items = new ArrayList<MealItem>();
        totalNutrients = new MealNutrients();
    }
}
