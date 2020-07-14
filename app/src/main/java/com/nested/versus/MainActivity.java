package com.nested.versus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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


    /*
    Function to initialize categoryList arrayList
     */
    public void createCategoryList() {

        // Initializes new arrayList
        Cursor data = mDatabaseHelper.getData("type_table");
        categoryList = new ArrayList<>(data.getCount());

        // Instantiate the list from local SQLite database
        while(data.moveToNext()) {
            if (data.getString(1) != null) {
                categoryList.add(new VersusCategory(data.getString(0), data.getString(1)));
            }
        }
        data.close();
    }

    /*
    Function to build recyclerView using categoryList
     */
    public void buildRecyclerView() {
        itemRecyclerView = findViewById(R.id.categoryView);
        itemRecyclerView.setHasFixedSize(true);
        itemLayoutManager = new LinearLayoutManager(this);
        categoryAdapter = new CategoryAdapter(categoryList);

        categoryTextView = findViewById(R.id.categoryNameTextView);
        dialog = new AlertDialog.Builder(this).create();
        categoryEditText = new EditText(this);

        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onCategoryClick(int position) {
                openCategory(categoryList.get(position).getCategoryName(), categoryList.get(position).getCategoryID());
            }

            @Override
            public void onEditClick(int position) {
                final int category_Position = position;
                final String old_Name = categoryList.get(position).getCategoryName();

                // Dialog for typing new name for the category
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                final EditText categoryEditText = new EditText(MainActivity.this);
                categoryEditText.setHint(categoryList.get(position).getCategoryName());

                dialog.setTitle("Edit Category Name: \n");
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

                        if (old_Name.equals(newName)) {
                            dialog.dismiss();
                        } else {
                            // Update categoryList for SQLite
                            if (mDatabaseHelper.renameTable(old_Name, newName)) {

                                // Update name in type_table
                                boolean isUpdated = mDatabaseHelper.updateData("type_table", categoryList.get(category_Position).getCategoryID(), "name", newName);

                                if(isUpdated) {
                                    // Update categoryList and adapter
                                    categoryList.get(category_Position).setCategoryName(newName);
                                    categoryAdapter.notifyDataSetChanged();

                                    // Rename Count&Image table
                                    mDatabaseHelper.renameTable(old_Name + "_Count", newName + "_Count");
                                    mDatabaseHelper.renameTable(old_Name + "_Image", newName + "_Image");
                                } else {
                                    // If newItemName is duplicate, show fail message
                                    AlertDialog newDialog = new AlertDialog.Builder(MainActivity.this).create();

                                    newDialog.setTitle("Unknown Error Occurred!");

                                    newDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    newDialog.show();
                                }

                            } else {

                                // If newItemName is duplicate, show fail message
                                AlertDialog newDialog = new AlertDialog.Builder(MainActivity.this).create();

                                newDialog.setTitle("The category already exists, OR can't contain special characters.");

                                newDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                newDialog.show();
                            }
                        }
                    }
                });

                dialog.show();
            }

            @Override
            public void onDeleteClick(int position) {

                // Delete category from the local database
                String oldName = categoryList.get(position).getCategoryName();
                mDatabaseHelper.deleteData("type_table", categoryList.get(position).getCategoryID());

                // Drop all tables for items
                mDatabaseHelper.deleteTable(categoryList.get(position).getCategoryName());
                mDatabaseHelper.deleteTable(categoryList.get(position).getCategoryName() + "_Count");
                mDatabaseHelper.deleteTable(categoryList.get(position).getCategoryName() + "_Image");

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
                Cursor data = mDatabaseHelper.getData("type_table");
                String newItemName;
                if (data.getCount() == 0) {
                    newItemName = "New_Item" + 1;
                } else {
                    data.moveToLast();
                    newItemName = "New_Item" + (Integer.parseInt(data.getString(0)) + 1);
                }

                // Check if there is duplicate name, and add if not
                if (mDatabaseHelper.addTable(newItemName)) {
                    boolean insertData = mDatabaseHelper.addData("type_table", newItemName);
                    data = mDatabaseHelper.getData("type_table");
                    data.moveToLast(); // To set new category's id below

                    if (insertData) {
                        // Add a new category into categoryList
                        categoryList.add(new VersusCategory(data.getString(0), newItemName));
                        categoryAdapter.notifyDataSetChanged();

                        // Add a new null image and countData into db
                        mDatabaseHelper.insertImage(newItemName + "_Image", null);

                        // Add a countData into db
                        mDatabaseHelper.addCountData(newItemName + "_Count", "0");

                        Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT);
                    }
                } else {
                    // If newItemName is duplicate, show fail message
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();

                    dialog.setTitle("The category ALREADY EXISTS!");

                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                data.close();
            }
        });


    }

    /*
    Function to move to each category activity
     */
    private void openCategory(String category_Name, String category_ID) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category_Name", category_Name);
        intent.putExtra("category_ID", category_ID);

        startActivity(intent);
    }
}

// TO CHECK DATABASE AND ITEMLIST
//System.out.println("======== Type_TABLE =========");
//Cursor test_Data = mDatabaseHelper.getData("type_table");
//while(test_Data.moveToNext()) {
//    System.out.println(test_Data.getString(0) + ", " + test_Data.getString(1));
//}
//System.out.println("=============================");
//test_Data.close();
//
//System.out.println("======== " + newItemName + " =========");
//Cursor test_Data2 = mDatabaseHelper.getData(newItemName);
//while(test_Data.moveToNext()) {
//    System.out.println(test_Data2.getString(0) + ", " + test_Data2.getString(1));
//}
//System.out.println("=============================");
//test_Data2.close();
//
//System.out.println("======== CategoryList =========");
//for (int i = 0; i< categoryList.size(); i++) {
//    System.out.println(categoryList.get(i).getCategoryID() + ", " + categoryList.get(i).getCategoryName());
//}
//System.out.println("=============================");