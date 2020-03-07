package com.example.android.meallogger.data;

public class LabelNutrients {
    public NutritionalValue fat;
    public NutritionalValue saturatedFat;
    public NutritionalValue transFat;
    public NutritionalValue cholesterol;
    public NutritionalValue sodium;
    public NutritionalValue carbohydrates;
    public NutritionalValue sugars;
    public NutritionalValue protein;
    public NutritionalValue calcium;
    public NutritionalValue iron;
    public NutritionalValue calories;

    LabelNutrients(){
        fat = new NutritionalValue();
        saturatedFat = new NutritionalValue();
        transFat = new NutritionalValue();
        cholesterol = new NutritionalValue();
        sodium = new NutritionalValue();
        carbohydrates = new NutritionalValue();
        sugars = new NutritionalValue();
        protein = new NutritionalValue();
        calcium = new NutritionalValue();
        iron = new NutritionalValue();
        calories = new NutritionalValue();
    }
}
