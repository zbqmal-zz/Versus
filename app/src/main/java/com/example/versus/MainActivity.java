package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<VersusItem> itemList;

    private RecyclerView itemRecyclerView;
    private itemAdapter itemAdapter;
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
                itemList.add(new VersusItem("New Item"));
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

    public void createItemList() {
        itemList = new ArrayList<VersusItem>();
    }

    public void buildRecyclerView() {
        itemRecyclerView = findViewById(R.id.itemView);
        itemRecyclerView.setHasFixedSize(true);
        itemLayoutManager = new LinearLayoutManager(this);
        itemAdapter = new itemAdapter(itemList);

        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(itemAdapter);

        itemAdapter.setOnItemClickListener(new itemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                itemList.remove(position);
                itemAdapter.notifyItemRemoved(position);
            }
        });
    }
}
