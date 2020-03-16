package com.example.android.meallogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.meallogger.data.Meal;
import com.example.android.meallogger.data.MealData;
import com.example.android.meallogger.data.Status;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SavedMealsAdapter.OnSavedMealClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SavedMealsAdapter mSavedMealsAdapter;
    private RecyclerView mSavedMealsRV;
    private TextView mErrorMessageTV;
    private ProgressBar mLoadingIndicatorPB;

    public static SavedMealsViewModel mViewModel;

    public List<MealData> mSavedMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSavedMealsAdapter = new SavedMealsAdapter(this);
        mSavedMealsRV = findViewById(R.id.rv_meal_items);
        mErrorMessageTV = findViewById(R.id.tv_error_message);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        mSavedMealsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedMealsRV.setHasFixedSize(true);
        mSavedMealsRV.setAdapter(mSavedMealsAdapter);

        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(
                        getApplication()
                )
        ).get(SavedMealsViewModel.class);

        mViewModel.getAllMeals().observe(this, new Observer<List<MealData>>() {
            @Override
            public void onChanged(List<MealData> meals) {
                mSavedMeals = meals;
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

        ItemTouchHelper.Callback SimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mViewModel.deleteSavedMeal(mSavedMeals.get(viewHolder.getAdapterPosition()));
                ((SavedMealsAdapter.SavedMealsViewHolder)viewHolder).remove();
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(SimpleCallback);
        helper.attachToRecyclerView(mSavedMealsRV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create_meal:
                Intent intent = new Intent(this, CreateMealActivity.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_new_food:
                Log.d(TAG, "new food");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSavedMealClicked(MealData meal) {
        Log.d(TAG, "saved meal clicked");
        // will add intent to open meal detail activity later
        Intent intent = new Intent(this, SavedMealDetailActivity.class);
        intent.putExtra("data", meal);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            mViewModel.insertMeal((MealData) data.getExtras().getSerializable("result"));
        }
    }

    // need method to read saved meals from database
}
