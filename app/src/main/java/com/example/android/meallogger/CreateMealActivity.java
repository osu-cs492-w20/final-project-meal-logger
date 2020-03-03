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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.meallogger.data.APIQueryTask;
import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.utils.UsdaAPIUtils;

import java.util.ArrayList;

public class CreateMealActivity extends AppCompatActivity implements APIQueryTask.Callback {
    FrameLayout mAddItemFrame;
    EditText mAddItemTextBox;
    Boolean mAddModuleVisibile;
    MenuItem mShowAddModuleButton;

    private static final String TAG = CreateMealActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAddItemFrame = findViewById(R.id.fl_add_item);
        mAddItemFrame.setVisibility(View.INVISIBLE);
        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mAddModuleVisibile = false;

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

    public void handleSearchResults(ArrayList<FoodId> json){
        Log.d(TAG, "!==="+json.get(0).description+" : "+json.get(0).fdcId);
    }

    private void addFoodItemtoMeal(String query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodSearchURL(query);
            Log.d(TAG, "!==="+url);
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
}
