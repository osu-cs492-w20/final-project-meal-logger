package com.example.android.meallogger;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.meallogger.data.FoodId;

import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealData;
import com.example.android.meallogger.data.MealItem;
import com.example.android.meallogger.data.PortionDescription;
import com.example.android.meallogger.data.Status;
import com.example.android.meallogger.utils.MealCreationViewModel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;
public class CreateMealActivity extends AppCompatActivity implements FoodidRecyclerAdapter.OnResultClickListener, MealitemRecyclerAdapter.OnResultClickListener, MeasureMealItemDialog.OnSubmitServingSize{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    
    private FrameLayout mAddItemFrame;
    private FrameLayout mTitleFrame;
    private FrameLayout mImageFrame;
    private EditText mAddItemTextBox;
    private EditText mTitleTextBox;
    private ImageView mImageView;
    private ImageView mSearchIcon;
    private Boolean mAddModuleVisibile;
    private MealCreationViewModel mViewModel;
    private RecyclerView mRvChoices;
    private FoodidRecyclerAdapter mRvChoiceAdapter;
    private ProgressBar mPbSearch;
    private ImageButton mButtonAddItem;
    private ImageButton mButtonTitleCfrm;
    private ImageButton mCameraButton;
    private List<FoodId> mChoiceContent;
    private Bitmap mImageBitmap;

    private FrameLayout mFlRvChoice;
    private Toast mToast;

    private Meal mFinalMeal;
    private TextView mTitle;
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

    private ImageButton mShowAddModuleButton;
    private int mFoodSelectedForPortion;

    private static final String TAG = CreateMealActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mImageView = findViewById(R.id.mealPic);
        mImageBitmap = null;

        mRvChoices = findViewById(R.id.rv_creation_choices);
        mRvChoices.setLayoutManager(new LinearLayoutManager(this));

        mFlRvChoice = findViewById(R.id.fl_choice_rv);

        mRvChoiceAdapter = new FoodidRecyclerAdapter(this);
        mRvChoices.setAdapter(mRvChoiceAdapter);

        mRvAddedItems = findViewById(R.id.rv_meal_items);
        mRvAddedItems.setLayoutManager(new LinearLayoutManager(this));
        mRvAddAdapter = new MealitemRecyclerAdapter(this);
        mRvAddedItems.setAdapter(mRvAddAdapter);
        mRvAddedItems.setItemAnimator(new DefaultItemAnimator());

        mImageFrame = findViewById(R.id.fl_meal_image);
        mAddItemFrame = findViewById(R.id.fl_add_item);
        mAddItemFrame.setVisibility(View.GONE);
        mTitleFrame = findViewById(R.id.fl_title_meal);
        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mTitleTextBox = findViewById(R.id.et_title_box);
        // Initial SUCCESS status change will switch this to false
        mAddModuleVisibile = false;
        mPbSearch = findViewById(R.id.pb_add_item);
        mButtonAddItem = findViewById(R.id.button_add_item);
        mButtonTitleCfrm = findViewById(R.id.button_title_confirm);
        mSearchIcon = findViewById(R.id.search_foodid_icon);
        mShowAddModuleButton = findViewById(R.id.action_new_food);

        mFinalMeal = null;
        mTitle = findViewById(R.id.tv_meal_title);
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

        mToast = null;

        mViewModel = new ViewModelProvider(this).get(MealCreationViewModel.class);
        mViewModel.getMeal().observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                if(meal!=null){
                    mFinalMeal=meal;
                    if(mFinalMeal.title.length()>0){
                        mTitle.setText(mFinalMeal.title);
                    }
                    Log.d(TAG, "!==Items:"+mFinalMeal.items.size());
                }
                updateNutrientDisplay();
            }
        });
        mViewModel.getFoodChoices().observe(this, new Observer<List<FoodId>>() {
            @Override
            public void onChanged(List<FoodId> foodIds) {
                if(foodIds != null){
                    mChoiceContent = foodIds;
                    dispatchToast("Results: "+foodIds.size());
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
                    showModule();
                    mAddItemTextBox.setEnabled(true);
                    mAddItemTextBox.setVisibility(View.VISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);
                    mRvAddedItems.scrollToPosition(0);
//                    mRvAddAdapter.updateAdapter(mFinalMeal.items);
                //FIX: Pretty animation but causes dupe
                    mRvAddAdapter.insertAdapter(mFinalMeal.items.get(0));
                } else if(status == Status.DONE){
                    //Hide TextBox & Pic
                    mAddItemTextBox.setVisibility(View.INVISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    //Show
                    //Pass choice into RV
                    mFlRvChoice.setVisibility(View.VISIBLE);
                    mRvChoiceAdapter.updateAdapter(mChoiceContent);
                    mRvChoices.scrollToPosition(0);
                } else{
                    mAddItemTextBox.setVisibility(View.VISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);
                    mFlRvChoice.setVisibility(View.GONE);
                    mAddItemTextBox.setEnabled(true);
                    dispatchToast("Failed Query!");
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
        mButtonTitleCfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mTitleTextBox.getText().toString();
                if(newTitle!=""){
                    mViewModel.rename(newTitle);
                }
                mTitleTextBox.setVisibility(View.INVISIBLE);
                mButtonTitleCfrm.setVisibility(View.INVISIBLE);
                mTitle.setVisibility(View.VISIBLE);
            }
        });
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setVisibility(View.INVISIBLE);
                mTitleTextBox.setVisibility(View.VISIBLE);
                mButtonTitleCfrm.setVisibility(View.VISIBLE);
            }
        });

        mShowAddModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModule();
            }
        });

        ItemTouchHelper.Callback SimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mViewModel.remove(viewHolder.getAdapterPosition());
                ((MealitemRecyclerAdapter.ResultViewHolder)viewHolder).remove();
                updateNutrientDisplay();
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(SimpleCallback);
        helper.attachToRecyclerView(mRvAddedItems);

//        test();
    }

    @Override
    public void onChoiceClicked(FoodId food){
        mFlRvChoice.setVisibility(View.GONE);
        mAddItemTextBox.setVisibility(View.VISIBLE);
        mPbSearch.setVisibility(View.VISIBLE);
        mViewModel.getItemDetails(food);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_finish_meal:
                saveMealData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showModule (){
        mAddModuleVisibile=!mAddModuleVisibile;
        if(mAddModuleVisibile){
            mShowAddModuleButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mShowAddModuleButton.setImageResource(R.drawable.ic_close_white_24dp);
            mAddItemTextBox.setText("");
            mTitleFrame.setVisibility(View.GONE);
            mImageView.setVisibility(View.INVISIBLE);
            if(mCameraButton.getVisibility() != View.GONE){
                mCameraButton.setVisibility(View.INVISIBLE);
            }
//            mShowAddModuleButton.setVisibility(View.INVISIBLE);
            mSearchIcon.setVisibility(View.VISIBLE);
            mAddItemFrame.setVisibility(View.VISIBLE);
        } else{
            mShowAddModuleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mShowAddModuleButton.setImageResource(R.drawable.ic_add_white_24dp);
            mAddItemFrame.setVisibility(View.GONE);
            mSearchIcon.setVisibility(View.INVISIBLE);
            if(mCameraButton.getVisibility() != View.GONE){
                mCameraButton.setVisibility(View.VISIBLE);
            }
//            mShowAddModuleButton.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            mTitleFrame.setVisibility(View.VISIBLE);
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

    private void saveMealData() {

        Intent returnIntent = new Intent();
        if(mFinalMeal!=null && mFinalMeal.items.size() != 0) {

            // Need to transfer all the data to MealData

            String foods = "";
            MealData newMeal = new MealData();
            newMeal.totalFat = mFinalMeal.totalNutrients.fat.amount + mFinalMeal.totalNutrients.fat.unit;
            newMeal.totalSatFat = mFinalMeal.totalNutrients.saturatedFat.amount + mFinalMeal.totalNutrients.saturatedFat.unit;
            newMeal.totalCholesterol = mFinalMeal.totalNutrients.cholesterol.amount + mFinalMeal.totalNutrients.cholesterol.unit;
            newMeal.totalSodium = mFinalMeal.totalNutrients.sodium.amount + mFinalMeal.totalNutrients.sodium.unit;
            newMeal.totalCarb = mFinalMeal.totalNutrients.carbohydrates.amount + mFinalMeal.totalNutrients.carbohydrates.unit;
            newMeal.totalSugar = mFinalMeal.totalNutrients.sugars.amount + mFinalMeal.totalNutrients.sugars.unit;
            newMeal.totalProtein = mFinalMeal.totalNutrients.protein.amount + mFinalMeal.totalNutrients.protein.unit;
            newMeal.totalCalcium = mFinalMeal.totalNutrients.calcium.amount + mFinalMeal.totalNutrients.calcium.unit;
            newMeal.totalIron = mFinalMeal.totalNutrients.iron.amount + mFinalMeal.totalNutrients.iron.unit;
            newMeal.totalCalories = mFinalMeal.totalNutrients.calories.amount + mFinalMeal.totalNutrients.calories.unit;

            foods += mFinalMeal.items.get(0).description;
            for (int x = 1; x < mFinalMeal.items.size() - 1; x++) {
                foods = foods + "," + mFinalMeal.items.get(x).description;
            }
            newMeal.foodsList = foods;

            Date date = new Date();
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat hourFormat =
                    new SimpleDateFormat("hh");
            SimpleDateFormat mFormat =
                    new SimpleDateFormat("a");

            String hour = hourFormat.format(date);
            String type = "Unknown";
            String ampm = mFormat.format(date);
            int h = parseInt(hour);
            if (ampm.equals("PM")) {
                h += 12;
            }
            Log.d(TAG, "value is: " + h);
            if (h > 15) {
                type = "Dinner";
            } else if (h > 12) {
                type = "Lunch";
            } else if (h > 0) {
                type = "Breakfast";
            }
            newMeal.description = type + " on " + dateFormat.format(date);
            newMeal.date = dateFormat.format(date);
            newMeal.time = timeFormat.format(date);
            newMeal.type = type;

            if (mImageBitmap != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();

                newMeal.photo = bArray;
            } else {
                newMeal.photo = new byte[0];
            }

            newMeal.name = mFinalMeal.title;
            returnIntent.putExtra("result", newMeal);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else{
            dispatchToast("Need 1 or more items");
        }
        //send error about problem saving
        //ADD DESCRIPTION PHOTO

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mCameraButton.setVisibility(View.GONE);
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void updateNutrientDisplay(){
        TextView unit;
        mCalories.setText(String.valueOf(mFinalMeal.totalNutrients.calories.amount));
        unit = findViewById(R.id.tv_total_unit_calories);
        unit.setText(mFinalMeal.totalNutrients.calories.unit);
        mProtein.setText(String.valueOf(mFinalMeal.totalNutrients.protein.amount));
        mCarbohydrates.setText(String.valueOf(mFinalMeal.totalNutrients.carbohydrates.amount));
        mFats.setText(String.valueOf(mFinalMeal.totalNutrients.fat.amount));
        mSfat.setText(String.valueOf(mFinalMeal.totalNutrients.saturatedFat.amount));
        mTfat.setText(String.valueOf(mFinalMeal.totalNutrients.transFat.amount));
        mSugars.setText(String.valueOf(mFinalMeal.totalNutrients.sugars.amount));
        mCalcium.setText(String.valueOf(mFinalMeal.totalNutrients.calcium.amount));
        mIron.setText(String.valueOf(mFinalMeal.totalNutrients.iron.amount));
        mSodium.setText(String.valueOf(mFinalMeal.totalNutrients.sodium.amount));
        mCholesterol.setText(String.valueOf(mFinalMeal.totalNutrients.cholesterol.amount));
    }

    @Override
    public void onItemSelected(MealItem item, int index) {
        mFoodSelectedForPortion = index;
        for(PortionDescription portion : item.foodPortions){
            portion.dataType = item.dataType;
        }
        DialogFragment dialog = new MeasureMealItemDialog(item, this);
        dialog.show(getSupportFragmentManager(), "GetPortionDialog");
    }

    public void calculatePortions(int index, float amountOfServing) {
        mViewModel.updatePortionMultiplier(mFoodSelectedForPortion, index, amountOfServing);
        mRvAddAdapter.updateAdapter(mFinalMeal.items);
    }

    public void dispatchToast(String toastText){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        mToast.show();
    }



    public void test(){
        mViewModel.addItemToMeal("Broccoli");
    }
}