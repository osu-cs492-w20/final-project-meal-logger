package com.example.android.meallogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android.meallogger.data.APIQueryTask;
import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.LabelNutrients;
import com.example.android.meallogger.data.MealItem;
import com.example.android.meallogger.utils.UsdaAPIUtils;

import java.util.ArrayList;

public class CreateMealActivity extends AppCompatActivity implements APIQueryTask.Callback {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FrameLayout mAddItemFrame;
    EditText mAddItemTextBox;
    Boolean mAddModuleVisibile;
    MenuItem mShowAddModuleButton;

    TextView mCalories;
    TextView mProtein;
    TextView mCarbohydrates;


    private static final String TAG = CreateMealActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAddItemFrame = findViewById(R.id.fl_add_item);
        mAddItemFrame.setVisibility(View.INVISIBLE);
        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mAddModuleVisibile = false;

        mCalories = findViewById(R.id.tv_total_calories);
        mProtein = findViewById(R.id.tv_total_protein);
        mCarbohydrates = findViewById(R.id.tv_total_carbs);


        Button searchButton = findViewById(R.id.button_add_item);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mAddItemTextBox.getText().toString();
                if (!TextUtils.isEmpty(query)){
                    addFoodItemtoMeal(query);
                }
            }
        });

        ImageButton cameraButton = findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_meal, menu);
        mShowAddModuleButton = findViewById(R.id.action_new_food);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_food:
                showModule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 1/2 CallBack function for APIQueryTask
    // Gets ID, fetches food from ID
    public void handleSearchResults(ArrayList<FoodId> json){
        showModule();
        //For now this selects the first result avoiding the "Survey" type bc different JSON.
        //We can change this later, so the user can choose based on brand
        FoodId foodItem = json.get(0);
        int i = 0;
        while(!foodItem.dataType.equals("Branded")){
            foodItem = json.get(i++);
        }
        String url = UsdaAPIUtils.buildFoodDetailsURL(foodItem.fdcId);
        Log.d(TAG, "!===Querying:"+foodItem.fdcId+"\n"+url);
        new APIQueryTask(this).execute(url, "detail");
    }
    // 2/2 CallBack function for APIQueryTask
    // Get food details
    public void handleDetailResults(MealItem json){
        Log.d(TAG, "!===Food:"+json.description
                +"\nCalories:"+json.labelNutrients.calories.value
                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);
        mCalories.setText(String.valueOf(json.labelNutrients.calories.value));
        mProtein.setText(String.valueOf(json.labelNutrients.protein.value));
        mCarbohydrates.setText(String.valueOf(json.labelNutrients.carbohydrates.value));
    }

    private void addFoodItemtoMeal(String query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodSearchURL(query);
            Log.d(TAG, "!===Searching: "+query+"\n"+url);
            new APIQueryTask(this).execute(url, "search");
        } else{
            Log.d(TAG, "!===Failed addFoodItem(): No Query");
        }
    }

    private void showModule (){
        mAddModuleVisibile=!mAddModuleVisibile;
        if(mAddModuleVisibile){
//            mShowAddModuleButton.setIcon(R.drawable.ic_close_white_24dp);
            mAddItemFrame.setVisibility(View.VISIBLE);
        } else{
//            mShowAddModuleButton.setIcon(R.drawable.ic_add_white_24dp);
            mAddItemFrame.setVisibility(View.INVISIBLE);
        }
    }

    private void dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        200);
            }
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.mealPic);
            imageView.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
