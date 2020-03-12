package com.example.android.meallogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.meallogger.data.FoodId;
import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.SavedMealsAdapter;
import com.example.android.meallogger.data.Status;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SavedMealsAdapter.OnSavedMealClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SavedMealsAdapter mSavedMealsAdapter;
    private RecyclerView mSavedMealsRV;
    private TextView mErrorMessageTV;
    private ProgressBar mLoadingIndicatorPB;

    private SavedMealsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSavedMealsAdapter = new SavedMealsAdapter(this);
        mSavedMealsRV = findViewById(R.id.rv_meal_items);
        mErrorMessageTV = findViewById(R.id.tv_error_message);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        mViewModel = new ViewModelProvider(this).get(SavedMealsViewModel.class);

        mViewModel.getSavedMeals().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                mSavedMealsAdapter.updateSavedMeals(meals);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if(status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if(status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mSavedMealsRV.setVisibility(View.VISIBLE);
                    mErrorMessageTV.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mSavedMealsRV.setVisibility(View.INVISIBLE);
                    mErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

//        mRvAdapter = new FoodidRecyclerAdapter();
//
//        mItemRv.setAdapter(mRvAdapter);
//        mItemRv.setLayoutManager(new LinearLayoutManager(this));
//        mItemRv.setHasFixedSize(true);
//        Meal newlyCreatedMeal = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_meal, menu);
        return true;
    }

//    @Override uncomment this after the method is implemented
    public boolean onCreateMealSelected(@NonNull MenuItem item) {
        // put the switch statement/create intent stuff for the create meal activity here
        // Intent intent = new Intent(this, CreateMealActivity.class);
        // startActivity(intent);
        return true;
    }

    @Override
    public void onSavedMealClicked(Meal meal) {
        Log.d(TAG, "saved meal clicked");
        // will add intent to open meal detail activity later
    }

// need method to read saved meals from database

}
