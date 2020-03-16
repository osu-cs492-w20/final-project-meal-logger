package com.example.android.meallogger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.android.meallogger.data.MealData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class SavedMealDetailActivity extends AppCompatActivity {
    MealData mSavedMeal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mSavedMeal = (MealData)getIntent().getSerializableExtra("data");
        loadContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_share_meal){
            shareMeal();
            return true;
        }
        return false;
    }

    private void shareMeal(){
        LinearLayout label = findViewById(R.id.meal_detail_label);
        Bitmap mealDetailsBitmap = Bitmap.createBitmap(
                label.getMeasuredWidth(),
                label.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas detailImage = new Canvas(mealDetailsBitmap);
        label.layout(label.getLeft(), label.getTop(), label.getRight(), label.getBottom());
        label.draw(detailImage);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, writeImage(mealDetailsBitmap));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Share meal label:"));
    }

    private Uri writeImage(Bitmap image) {
        File folder = new File(getCacheDir(), "shared");
        Uri uri = null;
        try{
            folder.mkdirs();
            File file = new File(folder, "meal_label.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            uri = FileProvider.getUriForFile(this, "com.meallogger.fileprovider", file);
        } catch (IOException e){

        }
        return uri;
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
