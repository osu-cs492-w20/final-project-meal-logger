package com.example.android.meallogger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.meallogger.data.MealData;

import java.io.ByteArrayOutputStream;

public class SavedMealDetailActivity extends AppCompatActivity {
    MealData mSavedMeal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mSavedMeal = (MealData)getIntent().getSerializableExtra("data");
        loadContent();
    }

    private void loadContent(){
        Bitmap bMap = BitmapFactory.decodeByteArray(mSavedMeal.photo, 0, mSavedMeal.photo.length);
        ImageView photo = findViewById(R.id.detail_meal_picture);
        photo.setImageBitmap(bMap);
        TextView view = findViewById(R.id.detail_name_tv);
        view.setText(mSavedMeal.name);
        view = findViewById(R.id.detail_desc_tv);
        view.setText(mSavedMeal.description);
        view = findViewById(R.id.detail_time_tv);
        view.setText(mSavedMeal.time);
        view = findViewById(R.id.detail_list_tv);
        view.setText(mSavedMeal.foodsList);
        view = findViewById(R.id.detail_calories_tv);
        view.setText(mSavedMeal.totalCalories);
        view = findViewById(R.id.detail_protein_tv);
        view.setText(mSavedMeal.totalProtein);
        view = findViewById(R.id.detail_carbohydrates_tv);
        view.setText(mSavedMeal.totalCarb);
        view = findViewById(R.id.detail_fat_tv);
        view.setText(mSavedMeal.totalFat);
        view = findViewById(R.id.detail_saturatedfat_tv);
        view.setText(mSavedMeal.totalSatFat);
        view = findViewById(R.id.detail_transfat_tv);
        view.setText(mSavedMeal.totalTransFat);
        view = findViewById(R.id.detail_sugar_tv);
        view.setText(mSavedMeal.totalSugar);
        view = findViewById(R.id.detail_calcium_tv);
        view.setText(mSavedMeal.totalCalcium);
        view = findViewById(R.id.detail_iron_tv);
        view.setText(mSavedMeal.totalIron);
        view = findViewById(R.id.detail_sodium_tv);
        view.setText(mSavedMeal.totalSodium);
        view = findViewById(R.id.detail_cholesterol_tv);
        view.setText(mSavedMeal.totalCholesterol);
    }

}
