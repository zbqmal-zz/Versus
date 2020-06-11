package com.example.versus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

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

        // SQLite Database
        mDatabaseHelper = new DatabaseHelper(this);

        // Setting recyclerView
        createCategoryList();
        buildRecyclerView();

        // Setting button widgets
        setButtons();
    }

    // Methods ...
    /*
    Function to initialize categoryList arrayList
     */
    public void createCategoryList() {

        // Initializes new arrayList
        Cursor data = mDatabaseHelper.getData();
        categoryList = new ArrayList<>(data.getCount());

        // Instantiate the list from local SQLite database
        while(data.moveToNext()) {
            if (data.getString(1) != null) {
                categoryList.add(new VersusCategory(data.getString(0), data.getString(1)));
            }
        }
    }

    /*
    Function to build recyclerView using categoryList
     */
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
                final int category_Position = position;

                // Dialog for typing new name for the category
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                final EditText categoryEditText = new EditText(MainActivity.this);

                dialog.setTitle("New Category Name: \n");
                dialog.setView(categoryEditText);

                // Cancel button
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Confirm button for editing
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = categoryEditText.getText().toString();

//                        // Update category adapter
//                        category.setCategoryName(newName);


                        // Update categoryList for SQLite
                        boolean isUpdated = mDatabaseHelper.updateData("type_table", categoryList.get(category_Position).getCategoryID(), newName);
                        if (isUpdated) {
                            // Update categoryList and adapter
                            categoryList.get(category_Position).setCategoryName(newName);
                            categoryAdapter.notifyDataSetChanged();

                            Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "ERROR Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }

            @Override
            public void onDeleteClick(int position) {

                // Delete category from the local database
                int isDeleted = mDatabaseHelper.deleteData("type_table", categoryList.get(position).getCategoryID());

                // Delete category from the list and adapter
                categoryList.remove(position);
                categoryAdapter.notifyItemRemoved(position);
            }
        });
    }

    /*
    Function to add new category
     */
    private void setButtons() {
        buttonAdd = findViewById(R.id.btn_ADD);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add a new category into the SQLite database
                boolean insertData = mDatabaseHelper.addData("New Item");
                Cursor data = mDatabaseHelper.getData();
                data.moveToLast();

                if (insertData) {
                    // Add a new category into categoryList
                    categoryList.add(new VersusCategory(data.getString(0), "New Item"));
                    categoryAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /*
    Function to move to each category activity
     */
    private void openCategory(String category_Name) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category_Name", category_Name);

        startActivity(intent);
    }
}
