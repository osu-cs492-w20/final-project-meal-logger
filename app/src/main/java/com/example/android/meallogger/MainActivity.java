package com.example.android.meallogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter mRvAdapter;
    private RecyclerView mItemRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mRvAdapter = new RecyclerAdapter();
//
//        mItemRv.setAdapter(mRvAdapter);
//        mItemRv.setLayoutManager(new LinearLayoutManager(this));
//        mItemRv.setHasFixedSize(true);
        Intent intent = new Intent(this, CreateMealActivity.class);
        startActivity(intent);
    }
}
