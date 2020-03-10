package com.example.android.meallogger.data;

public class MealNutrients {

    MealNutrients(){
        fat = new MealValue();
        fat.amount = 0;
        fat.unit = "g";
        saturatedFat = new MealValue();
        saturatedFat.amount = 0;
        saturatedFat.unit = "g";
        transFat = new MealValue();
        transFat.amount = 0;
        transFat.unit = "g";
        cholesterol = new MealValue();
        cholesterol.amount = 0;
        cholesterol.unit = "mg";
        sodium = new MealValue();
        sodium.amount = 0;
        sodium.unit = "mg";
        carbohydrates = new MealValue();
        carbohydrates.amount = 0;
        carbohydrates.unit = "g";
        sugars = new MealValue();
        sugars.amount = 0;
        sugars.unit = "g";
        protein = new MealValue();
        protein.amount = 0;
        protein.unit = "g";
        calcium = new MealValue();
        calcium.amount = 0;
        calcium.unit = "mg";
        iron = new MealValue();
        iron.amount = 0;
        iron.unit = "mg";
        calories = new MealValue();
        calories.amount = 0;
        calories.unit = "kcal";
    }

    public class MealValue{
        public float amount;
        public String unit;
    }

    public MealValue fat;
    public MealValue saturatedFat;
    public MealValue transFat;
    public MealValue cholesterol;
    public MealValue sodium;
    public MealValue carbohydrates;
    public MealValue sugars;
    public MealValue protein;
    public MealValue calcium;
    public MealValue iron;
    public MealValue calories;
}
