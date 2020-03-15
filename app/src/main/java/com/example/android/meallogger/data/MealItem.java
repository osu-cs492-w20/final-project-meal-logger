package com.example.android.meallogger.data;

import java.io.Serializable;
import java.util.List;

public class MealItem implements Serializable {
    public String description;
    public String dataType;
    public String brandOwner;
    public int fdcId;
    public float servingSize;
    public String servingSizeUnit;
    public String householdServingFullText;
    public List<FoodNutrient> foodNutrients;
    public List<PortionDescription> foodPortions;
    public float servingMultiplier;
    public float totalGramWeight;
    public int appliedPortionIndex;
    public float amountPortion;
}
