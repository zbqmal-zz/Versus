package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemRecyclerView;
    private RecyclerView.Adapter itemAdapter;
    private RecyclerView.LayoutManager itemLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<VersusItem> itemList = new ArrayList<VersusItem>();
        itemList.add(new VersusItem("Item1"));
        itemList.add(new VersusItem("Item2"));
        itemList.add(new VersusItem("Item3"));

        itemRecyclerView = findViewById(R.id.itemView);
        itemRecyclerView.setHasFixedSize(true);
        itemLayoutManager = new LinearLayoutManager(this);
        itemAdapter = new itemAdapter(itemList);

        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(itemAdapter);
    }
}
