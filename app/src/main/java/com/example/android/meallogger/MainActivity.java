package com.example.android.meallogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity {

    private FoodidRecyclerAdapter mRvAdapter;
    private RecyclerView mItemRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mRvAdapter = new FoodidRecyclerAdapter();
//
//        mItemRv.setAdapter(mRvAdapter);
//        mItemRv.setLayoutManager(new LinearLayoutManager(this));
//        mItemRv.setHasFixedSize(true);
        Intent intent = new Intent(this, CreateMealActivity.class);
//        Meal newlyCreatedMeal = null;
        startActivity(intent);
    }
}
