package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private ImageView btn_AddCol,
                      btn_AddRow;
    private TableLayout table_Row,
                        table_Items;
    private TableRow table_Col;
    private ArrayList<VersusItem> itemList;
    private ArrayList<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Set up current category name
        Intent intent = getIntent();
        String category_Name = intent.getStringExtra("category_Name");
        TextView categoryNameTextView = findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category_Name);

        createLists();
        setTables();
        setButtons();

    }

    /*
    Function for initializing item list
     */
    private void createLists() {

        itemList = new ArrayList<>();
        VersusItem newItem = new VersusItem("New Item");
        itemList.add(newItem);

        categoryList = new ArrayList<>();
        String newCategory = "New Category";
        categoryList.add(newCategory);
    }

    /*
    Function for setting up buttons' on click listeners
     */
    private void setButtons() {
        btn_AddCol = findViewById(R.id.image_Add_Col);
        btn_AddCol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView newTextView = new TextView(CategoryActivity.this);
                newTextView.setText("New Item");
                table_Col.addView(newTextView);
            }
        });

        btn_AddRow = findViewById(R.id.image_Add_Row);
        btn_AddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New table row
                TableRow newTableRow = new TableRow(CategoryActivity.this);
                newTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView newTextView = new TextView(CategoryActivity.this);
                newTextView.setText("Hello");

                // Add the new contents on the row and table
                newTableRow.addView(newTextView);
                table_Row.addView(newTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        });
    }

    /*
    Function for setting up tables
     */
    private void setTables() {
        table_Row = findViewById(R.id.table_row);
        table_Items = findViewById(R.id.table_items);
        table_Col = findViewById(R.id.table_col);
    }
}
