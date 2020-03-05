package com.example.android.meallogger;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.meallogger.data.APIQueryTask;
import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.LabelNutrients;
import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealItem;
import com.example.android.meallogger.data.Status;
import com.example.android.meallogger.utils.MealCreationViewModel;
import com.example.android.meallogger.utils.UsdaAPIUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateMealActivity extends AppCompatActivity implements RecyclerAdapter.OnResultClickListener{
    private FrameLayout mAddItemFrame;
    private EditText mAddItemTextBox;
    private Boolean mAddModuleVisibile;
    private MenuItem mShowAddModuleButton;
    private MealCreationViewModel mViewModel;
    private RecyclerView mRvChoices;
    private LinearLayout mRvHolder;
    private RecyclerAdapter mRvAdapter;
    private Status mSearchStatus;
    private ProgressBar mPbSearch;
    private ImageButton mButtonAddItem;
    private List<FoodId> mChoiceContent;

    private TextView mCalories;
    private TextView mProtein;
    private TextView mCarbohydrates;



    private static final String TAG = CreateMealActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mRvChoices = findViewById(R.id.rv_creation_choices);
//        mRvHolder = findViewById(R.id.ll_rv_holder);
        mRvChoices.setLayoutManager(new LinearLayoutManager(this));
        mRvChoices.setHasFixedSize(true);

        mRvAdapter = new RecyclerAdapter(this);
        mRvChoices.setAdapter(mRvAdapter);


        mAddItemFrame = findViewById(R.id.fl_add_item);
        mAddItemFrame.setVisibility(View.INVISIBLE);
        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mAddModuleVisibile = false;
        mPbSearch = findViewById(R.id.pb_add_item);
        mButtonAddItem = findViewById(R.id.button_add_item);

        mCalories = findViewById(R.id.tv_total_calories);
        mProtein = findViewById(R.id.tv_total_protein);
        mCarbohydrates = findViewById(R.id.tv_total_carbs);

        mViewModel = new ViewModelProvider(this).get(MealCreationViewModel.class);
        mViewModel.getMeal().observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                Log.d(TAG, "!===Changed: "+meal);
            }
        });
        mViewModel.getFoodChoices().observe(this, new Observer<List<FoodId>>() {
            @Override
            public void onChanged(List<FoodId> foodIds) {
                if(foodIds != null){

                }
            }
        });
        mViewModel.getStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                mSearchStatus = status;
                if(status == Status.LOADING){
                    mAddItemTextBox.setEnabled(false);
                    mPbSearch.setVisibility(View.VISIBLE);
                    mButtonAddItem.setVisibility(View.INVISIBLE);
                } else if(status == Status.SUCCESS){
                    mAddItemTextBox.setEnabled(true);
                    mAddItemTextBox.setVisibility(View.VISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);
                } else if(status == Status.DONE){
                    //Hide TextBox
                    mAddItemTextBox.setVisibility(View.INVISIBLE);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    //Pass choice into RV
                    mChoiceContent = new ArrayList<FoodId>();
                    mChoiceContent = mViewModel.getFoodChoices().getValue();
                    Log.d(TAG, "!===mChoiceContent:"+mChoiceContent);

                    mRvAdapter.updateAdapter(mChoiceContent);
                    //Show RV
                    mRvChoices.setVisibility(View.VISIBLE);
                    //On Click RV finish search
                }
                else{
                    mAddItemTextBox.setEnabled(true);
                    mPbSearch.setVisibility(View.INVISIBLE);
                    mButtonAddItem.setVisibility(View.VISIBLE);
                }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 1/2 CallBack function for APIQueryTask
    // Gets ID, fetches food from ID
//    public void handleSearchResults(ArrayList<FoodId> json){
//        showModule();
//        //For now this selects the first result avoiding the "Survey" type bc different JSON.
//        //We can change this later, so the user can choose based on brand
//        FoodId foodItem = json.get(0);
//        int i = 0;
//        while(!foodItem.dataType.equals("Branded")){
//            foodItem = json.get(i++);
//        }
//        String url = UsdaAPIUtils.buildFoodDetailsURL(foodItem.fdcId);
//        Log.d(TAG, "!===Querying:"+foodItem.fdcId+"\n"+url);
//        new APIQueryTask(this).execute(url, "detail");
//    }
    // 2/2 CallBack function for APIQueryTask
    // Get food details
//    public void handleDetailResults(MealItem json){
//        Log.d(TAG, "!===Food:"+json.description
//                +"\nCalories:"+json.labelNutrients.calories.value
//                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);
//        mCalories.setText(String.valueOf(json.labelNutrients.calories.value));
//        mProtein.setText(String.valueOf(json.labelNutrients.protein.value));
//        mCarbohydrates.setText(String.valueOf(json.labelNutrients.carbohydrates.value));
//    }

//    private void addFoodItemtoMeal(String query){
//        if(query!=null){
//            String url = UsdaAPIUtils.buildFoodSearchURL(query);
//            Log.d(TAG, "!===Searching: "+query+"\n"+url);
//            new APIQueryTask(mViewModel).execute(url, "search");
//        } else{
//            Log.d(TAG, "!===Failed addFoodItem(): No Query");
//        }
//    }

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
}
