package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
                // For item names
                TextView newItemTextView = new TextView(CategoryActivity.this);
                newItemTextView.setText("New Item");
                newItemTextView.setGravity(Gravity.CENTER);
                newItemTextView.setWidth(300);
                newItemTextView.setHeight(150);
                table_Col.addView(newItemTextView);
                itemList.add(new VersusItem("New Item"));

                // For item values
                for (int i = 0; i < categoryList.size(); i++) {
                    // TextView for value
                    TextView newValueTextView = new TextView(CategoryActivity.this);
                    newValueTextView.setGravity(Gravity.CENTER);
                    newValueTextView.setWidth(300);
                    newValueTextView.setHeight(100);
                    newValueTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    newValueTextView.setText("New Value");

                    // Add a cell on each row
                    TableRow currTableRow = (TableRow) table_Items.getChildAt(i);
                    currTableRow.addView(newValueTextView);
                }
            }
        });

        btn_AddRow = findViewById(R.id.image_Add_Row);
        btn_AddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For table_Row
                // New table row
                TableRow newTableRow = new TableRow(CategoryActivity.this);
                newTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView newTextView = new TextView(CategoryActivity.this);
                newTextView.setGravity(Gravity.CENTER);
                newTextView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                newTextView.setHeight(100);
                newTextView.setText("New Category");

                // Add the new contents on the row and table
                newTableRow.addView(newTextView);
                table_Row.addView(newTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                categoryList.add("New Category");

                // For item values
                // Create new each row
                TableRow newValueTableRow = new TableRow(CategoryActivity.this);
                newValueTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                // To add each new value
                for (int i = 0; i < itemList.size(); i++) {
                    TextView newValueTextView = new TextView(CategoryActivity.this);
                    newValueTextView.setGravity(Gravity.CENTER);
                    newValueTextView.setWidth(300);
                    newValueTextView.setHeight(100);
                    newValueTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    newValueTextView.setText("New Value");
                    newValueTableRow.addView(newValueTextView);
                }
                table_Items.addView(newValueTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
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
