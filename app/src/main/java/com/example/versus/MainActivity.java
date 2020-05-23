package com.example.versus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<VersusCategory> categoryList;

    private RecyclerView itemRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager itemLayoutManager;

    TextView categoryTextView;
    AlertDialog dialog;
    EditText categoryEditText;

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createCategoryList();
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

    public void createCategoryList() {
        categoryList = new ArrayList<>();
    }

    public void buildRecyclerView() {
        itemRecyclerView = findViewById(R.id.categoryView);
        itemRecyclerView.setHasFixedSize(true);
        itemLayoutManager = new LinearLayoutManager(this);
        categoryAdapter = new CategoryAdapter(categoryList);

        categoryTextView = (TextView) findViewById(R.id.categoryNameTextView);
        dialog = new AlertDialog.Builder(this).create();
        categoryEditText = new EditText(this);

        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onCategoryClick(int position) {
                openCategory(categoryList.get(position).getCategoryName());
            }

            @Override
            public void onEditClick(int position) {
                final VersusCategory category = categoryList.get(position);

                // Dialog for typing new name for the category
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                final EditText categoryEditText = new EditText(MainActivity.this);

                dialog.setTitle("New Category Name: \n");
                dialog.setView(categoryEditText);

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        category.setCategoryName(categoryEditText.getText().toString());
                        categoryAdapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }

            @Override
            public void onDeleteClick(int position) {
                categoryList.remove(position);
                categoryAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void openCategory(String category_Name) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category_Name", category_Name);

        startActivity(intent);
    }
}
