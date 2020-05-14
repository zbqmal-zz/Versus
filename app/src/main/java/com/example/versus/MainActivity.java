package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<VersusCategory> categoryList;

    private RecyclerView itemRecyclerView;
    private categoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager itemLayoutManager;

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createItemList();
        buildRecyclerView();

        setButtons();
    }

    private void setButtons() {
        buttonAdd = findViewById(R.id.btn_ADD);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryList.add(new VersusCategory("New Item"));
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    public void createItemList() {
        categoryList = new ArrayList<>();
    }

    public void buildRecyclerView() {
        itemRecyclerView = findViewById(R.id.categoryView);
        itemRecyclerView.setHasFixedSize(true);
        itemLayoutManager = new LinearLayoutManager(this);
        categoryAdapter = new categoryAdapter(categoryList);

        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new categoryAdapter.OnItemClickListener() {
            @Override
            public void onCategoryClick(int position) {
                openCategory(position);
            }

            @Override
            public void onDeleteClick(int position) {
                categoryList.remove(position);
                categoryAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void openCategory(int position) {
        Intent intent = new Intent(this, ItemActivity.class);
        startActivity(intent);
    }
}
