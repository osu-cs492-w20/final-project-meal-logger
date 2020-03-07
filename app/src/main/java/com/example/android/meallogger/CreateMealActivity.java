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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.meallogger.data.FoodId;

import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.Status;
import com.example.android.meallogger.utils.MealCreationViewModel;

import java.util.List;

public class CreateMealActivity extends AppCompatActivity implements FoodidRecyclerAdapter.OnResultClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    
    private FrameLayout mAddItemFrame;
    private FrameLayout mImageFrame;
    private EditText mAddItemTextBox;
    private ImageView mImageView;
    private Boolean mAddModuleVisibile;
    private MealCreationViewModel mViewModel;
    private RecyclerView mRvChoices;
    private FoodidRecyclerAdapter mRvChoiceAdapter;
    private ProgressBar mPbSearch;
    private ImageButton mButtonAddItem;
    private ImageButton mCameraButton;
    private List<FoodId> mChoiceContent;

    private Meal mFinalMeal;

    private RecyclerView mRvAddedItems;
    private MealitemRecyclerAdapter mRvAddAdapter;

    private TextView mCalories;
    private TextView mProtein;
    private TextView mCarbohydrates;
    private TextView mFats;
    private TextView mSfat;
    private TextView mTfat;
    private TextView mSugars;
    private TextView mCalcium;
    private TextView mIron;
    private TextView mSodium;
    private TextView mCholesterol;

    private View mShowAddModuleButton;

    private static final String TAG = CreateMealActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mImageView = findViewById(R.id.mealPic);

        mRvChoices = findViewById(R.id.rv_creation_choices);
        mRvChoices.setLayoutManager(new LinearLayoutManager(this));
//        mRvChoices.setHasFixedSize(true);

        mRvChoiceAdapter = new FoodidRecyclerAdapter(this);
        mRvChoices.setAdapter(mRvChoiceAdapter);

        mRvAddedItems = findViewById(R.id.rv_meal_items);
        mRvAddedItems.setLayoutManager(new LinearLayoutManager(this));
//        mRvAddedItems.setHasFixedSize(true);
        mRvAddAdapter = new MealitemRecyclerAdapter();
        mRvAddedItems.setAdapter(mRvAddAdapter);

        mImageFrame = findViewById(R.id.fl_meal_image);
        mAddItemFrame = findViewById(R.id.fl_add_item);
        mAddItemFrame.setVisibility(View.INVISIBLE);
        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mAddModuleVisibile = false;
        mPbSearch = findViewById(R.id.pb_add_item);
        mButtonAddItem = findViewById(R.id.button_add_item);

        mFinalMeal = null;

        mCalories = findViewById(R.id.tv_total_calories);
        mProtein = findViewById(R.id.tv_total_protein);
        mCarbohydrates = findViewById(R.id.tv_total_carbs);
        mFats = findViewById(R.id.tv_total_fat);
        mSfat = findViewById(R.id.tv_total_sfat);
        mTfat = findViewById(R.id.tv_total_tfat);
        mSugars = findViewById(R.id.tv_total_sugar);
        mCalcium = findViewById(R.id.tv_total_calcium);
        mIron = findViewById(R.id.tv_total_iron);
        mSodium = findViewById(R.id.tv_total_sodium);
        mCholesterol = findViewById(R.id.tv_total_cholst);

        mViewModel = new ViewModelProvider(this).get(MealCreationViewModel.class);
        mViewModel.getMeal().observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                if(meal!=null){
                    Log.d(TAG, "!===Changed: "+meal.totalNutrients.calories.value);
                    mFinalMeal=meal;
                }
            }
        });
        mViewModel.getFoodChoices().observe(this, new Observer<List<FoodId>>() {
            @Override
            public void onChanged(List<FoodId> foodIds) {
                if(foodIds != null){
                    Log.d(TAG, "!===Choices: "+foodIds.size());
                    mChoiceContent = foodIds;
                }
            }
        });
        mViewModel.getStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if(status == Status.LOADING){
                    mAddItemTextBox.setEnabled(false);
                    mPbSearch.setVisibility(View.VISIBLE);
                    mButtonAddItem.setVisibility(View.INVISIBLE);
                } else if(status == Status.SUCCESS){
                    mAddItemTextBox.setEnabled(true);
                    mAddItemTextBox.setVisibility(View.VISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);

                    mRvChoiceAdapter.clear();

                    if(mFinalMeal!=null){
                        mCalories.setText(String.valueOf(mFinalMeal.totalNutrients.calories.value));
                        mProtein.setText(String.valueOf(mFinalMeal.totalNutrients.protein.value));
                        mCarbohydrates.setText(String.valueOf(mFinalMeal.totalNutrients.carbohydrates.value));
                        mFats.setText(String.valueOf(mFinalMeal.totalNutrients.fat.value));
                        mSfat.setText(String.valueOf(mFinalMeal.totalNutrients.saturatedFat.value));
                        mTfat.setText(String.valueOf(mFinalMeal.totalNutrients.transFat.value));
                        mSugars.setText(String.valueOf(mFinalMeal.totalNutrients.sugars.value));
                        mCalcium.setText(String.valueOf(mFinalMeal.totalNutrients.calcium.value));
                        mIron.setText(String.valueOf(mFinalMeal.totalNutrients.iron.value));
                        mSodium.setText(String.valueOf(mFinalMeal.totalNutrients.sodium.value));
                        mCholesterol.setText(String.valueOf(mFinalMeal.totalNutrients.cholesterol.value));
                        // FIX THIS
                        mRvAddAdapter.updateAdapter(mFinalMeal.items);
                        //                        mRvAddedItems.setVisibility(View.VISIBLE);
                    }
                    mImageFrame.setVisibility(View.VISIBLE);
                    showModule();
                } else if(status == Status.DONE){
                    //Hide TextBox & Pic
                    mImageFrame.setVisibility(View.GONE);
                    mAddItemTextBox.setVisibility(View.INVISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    //Pass choice into RV
                    Log.d(TAG, "!===mChoiceContent:"+mChoiceContent);

                    mRvChoiceAdapter.updateAdapter(mChoiceContent);
                    //Show RV
                    mRvChoices.setVisibility(View.VISIBLE);
                    //On Click RV finish search
                }
                else{
                    mAddItemTextBox.setEnabled(true);
                    mAddItemTextBox.setVisibility(View.VISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);
                    mRvChoices.setVisibility(View.INVISIBLE);
                }
            }
        });

        mCameraButton = findViewById(R.id.button_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        mButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mAddItemTextBox.getText().toString();
                if (!TextUtils.isEmpty(query)){
                    mViewModel.addItemToMeal(query);
                }
            }
        });
    }

    @Override
    public void onRVClicked(FoodId food){
        Log.d(TAG, "!===More Details:"+food.fdcId);

        mRvChoices.setVisibility(View.GONE);
        mAddItemTextBox.setVisibility(View.VISIBLE);
        mPbSearch.setVisibility(View.VISIBLE);

        mViewModel.getItemDetails(food);
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
            case R.id.button_camera:
                dispatchTakePictureIntent();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showModule (){
        mAddModuleVisibile=!mAddModuleVisibile;
        if(mAddModuleVisibile){
//            mShowAddModuleButton.setIcon(R.drawable.ic_close_white_24dp);
            mAddItemTextBox.setText("");
            mAddItemFrame.setVisibility(View.VISIBLE);
            mRvAddedItems.setVisibility(View.GONE);

        } else{
//            mShowAddModuleButton.setIcon(R.drawable.ic_add_white_24dp);
            mAddItemFrame.setVisibility(View.GONE);
            mRvAddedItems.setVisibility(View.VISIBLE);

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
            mCameraButton.setVisibility(View.GONE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
