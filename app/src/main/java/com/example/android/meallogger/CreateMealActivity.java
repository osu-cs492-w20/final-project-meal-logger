package com.example.android.meallogger;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateMealActivity extends AppCompatActivity {
    EditText mAddItemTextBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mAddItemTextBox = findViewById(R.id.et_item_lookup_box);
        mAddItemTextBox.setVisibility(View.INVISIBLE);

        setContentView(R.layout.activity_create);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_meal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_food:
                addFoodItemtoMeal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addFoodItemtoMeal(){
        mAddItemTextBox.setVisibility(View.VISIBLE);
    }
}
