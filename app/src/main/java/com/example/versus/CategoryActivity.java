package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Set up current category name
        Intent intent = getIntent();
        String category_Name = intent.getStringExtra("category_Name");
        TextView categoryNameTextView = findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category_Name);
    }
}
