package com.example.versus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements HorizontalScroll.ScrollViewListener, VerticalScroll.ScrollViewListener {

    DatabaseHelper mDatabaseHelper;

    private String category_Name;
    private ImageView btn_AddItem,
            btn_AddCategory;
    private TableLayout table_Categories,
            table_Items;
    private TableRow table_ItemNames;
    private ArrayList<VersusItem> itemList;

    private HorizontalScroll itemName_Horizontal,
            itemValue_Horizontal;

    private VerticalScroll itemCategory_Vertical,
            itemValue_Vertical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // SQLite Database
        mDatabaseHelper = new DatabaseHelper(this);

        // Set up current category name
        Intent intent = getIntent();
        category_Name = intent.getStringExtra("category_Name");
        TextView categoryNameTextView = findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category_Name);

        // Setup double scrollViews
        setupScrolling();
        itemName_Horizontal.setScrollViewListener(this);
        itemValue_Horizontal.setScrollViewListener(this);
        itemValue_Vertical.setScrollViewListener(this);
        itemCategory_Vertical.setScrollViewListener(this);

        // Set Tables up
        setTables();

        // Set up add buttons
        setButtons();

        // Load or initialize itemList and Place all items in the UI
        init_ItemList(intent.getStringExtra("category_Name"));

        // TODO: Remove=================
        System.out.println("============= item List ==============");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.print(itemList.get(i).getItemID() + ". " + itemList.get(i).getItemName() + ", ");
            for (int j = 0; j < itemList.get(i).getItemValues().size(); j++) {
                System.out.print(itemList.get(i).getItemValues().get(j) + ", ");
            }
            System.out.println();
        }
        System.out.println("======================================");

        System.out.println("============= Database ===============");
        Cursor test_Data = mDatabaseHelper.getData(category_Name);
        while(test_Data.moveToNext()) {
            System.out.print(test_Data.getString(0) + ". " + test_Data.getString(1) + ", ");
            for (int i = 2; i < test_Data.getColumnCount(); i++) {
                System.out.print(test_Data.getColumnName(2) + ": " + test_Data.getString(2) + ", ");
            }
            System.out.println();
        }
        System.out.println("======================================");
        // TODO: =======================

    }

    @Override
    public void onScrollChanged(HorizontalScroll scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == itemName_Horizontal) {
            itemValue_Horizontal.scrollTo(x, y);
        } else if (scrollView == itemValue_Horizontal) {
            itemName_Horizontal.scrollTo(x, y);
        }
    }

    @Override
    public void onScrollChanged(VerticalScroll scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == itemCategory_Vertical) {
            itemValue_Vertical.scrollTo(x, y);
        } else if (scrollView == itemValue_Vertical) {
            itemCategory_Vertical.scrollTo(x, y);
        }
    }

    /*
    Function for initializing item list and placing them onto UI
     */
    private void init_ItemList(final String category_Name) {

        itemList = new ArrayList<VersusItem>();

        // Load itemList
        Cursor data = mDatabaseHelper.getData(category_Name);

        // If no data exists
        if (data.getCount() == 0) {

            // Create new VersusItem and push
//            VersusItem newItem = new VersusItem("0", "New Item");
//            itemList.add(newItem);

            // Add new item into the database
            mDatabaseHelper.addData(category_Name, "New Item"); // TODO: Fix it
//            mDatabaseHelper.addCategoryData(category_Name, "New Value");
        }

        // Set up itemList and Place all item names onto UI
        data = mDatabaseHelper.getData(category_Name);
        while (data.moveToNext()) {
            if (data.getString(1) != null) {

                String data_ID = data.getString(0);
                String data_Name = data.getString(1);

                // Place the item name onto UI
                TextView nameView = createTextViewForItemName(data_ID, data_Name, table_ItemNames);
                table_ItemNames.addView(nameView);

                // Instantiate each VersusItem
                VersusItem savedItem = new VersusItem(data_ID, data_Name);
                for (int i = 2; i < data.getColumnCount(); i++) {
                    String data_Category = data.getColumnName(i);
                    String data_Value = data.getString(i);

                    // Place item categories first if it's first time to add
                    if (data.getPosition() == 0) {
                        TableRow categoryView = createTableRowForItemCategory(data_Category, table_Categories);
                        table_Categories.addView(categoryView);
                    }


                    if (data.getPosition() == 0) {
                        // For ItemValues
                        // Create and add new TableRow into table_ItemValues, and Place each itemValue into the tableRow
                        TableRow newTableRow = new TableRow(CategoryActivity.this);
                        TextView valueTextView = createTextViewForItemValue(data_ID, data_Value, data_Category, i - 2);
                        newTableRow.addView(valueTextView);
                        table_Items.addView(newTableRow);
                    } else {
                        // For ItemValues (Not first items)
                        TextView newValueTextView = createTextViewForItemValue(data_ID, data_Value, data_Category, i - 2);
                        TableRow currentRow = (TableRow) table_Items.getChildAt(i - 2);
                        currentRow.addView(newValueTextView);
                    }

                    // Update the VersusItem
                    savedItem.getItemValues().add(data.getString(i));
                }

                // Push the item to the item list
                itemList.add(savedItem);
            }
        }
    }

    /*
    Method for double ScrollViews
     */
    private void setupScrolling() {

        itemName_Horizontal = findViewById(R.id.itemName_Horizontal);
        itemValue_Horizontal = findViewById(R.id.itemValue_Horizontal);
        itemCategory_Vertical = findViewById(R.id.itemCategory_Vertical);
        itemValue_Vertical = findViewById(R.id.itemValue_Vertical);

    }

    /*
    Function for setting up tables
     */
    private void setTables() {
        table_Categories = findViewById(R.id.table_row);
        table_Items = findViewById(R.id.table_items);
        table_ItemNames = findViewById(R.id.table_col);
    }

    /*
    Function for setting up buttons' on click listeners
     */
    private void setButtons() {
        btn_AddItem = findViewById(R.id.image_Add_Col);
        btn_AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. into DB
                Cursor data = mDatabaseHelper.getData(category_Name);
                mDatabaseHelper.addItemData(category_Name, "New Item", "New Value");

                // 2. into UI
                Integer newId = data.getCount() + 1;
                TextView newItemTextView = createTextViewForItemName(newId.toString(), "New Item", table_ItemNames);
                table_ItemNames.addView(newItemTextView);
                for (int i = 2; i < data.getColumnCount(); i++) {
                    TextView newValueTextView = createTextViewForItemValue(newId.toString(), "New Value", data.getColumnName(i), i - 2);

                    // Add a cell on each row
                    TableRow currTableRow = (TableRow) table_Items.getChildAt(i - 2);
                    currTableRow.addView(newValueTextView);
                }

                // 3. into itemList
                VersusItem newItem = new VersusItem(String.valueOf(data.getCount()), "New Item");
                for (int i = 2; i < data.getColumnCount(); i++) {
                    newItem.getItemValues().add("New Value");
                }
                itemList.add(newItem);


                // TODO: Remove
                System.out.println("============= item List ==============");
                for (int i = 0; i < itemList.size(); i++) {
                    System.out.print(itemList.get(i).getItemID() + ". " + itemList.get(i).getItemName() + ", ");
                    for (int j = 0; j < itemList.get(i).getItemValues().size(); j++) {
                        System.out.print(itemList.get(i).getItemValues().get(j) + ", ");
                    }
                    System.out.println();
                }
                System.out.println("======================================");

                System.out.println("============= Database ===============");
                Cursor test_Data = mDatabaseHelper.getData(category_Name);
                while(test_Data.moveToNext()) {
                    System.out.print(test_Data.getString(0) + ". " + test_Data.getString(1) + ", ");
                    for (int i = 2; i < test_Data.getColumnCount(); i++) {
                        System.out.print(test_Data.getColumnName(i) + ": " + test_Data.getString(i) + ", ");
                    }
                    System.out.println();
                }
                System.out.println("======================================");
                // TODO: ==================================================
            }
        });

        btn_AddCategory = findViewById(R.id.image_Add_Row);
        btn_AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. into DB
                Cursor data = mDatabaseHelper.getData(category_Name);
                data.moveToLast();
                String newCategoryName = "New_Category" + String.valueOf(data.getColumnCount() - 1);
                data.moveToFirst();
                System.out.println("new Category Name: " + newCategoryName);
                mDatabaseHelper.addColumn(category_Name, newCategoryName);

                // 2. into UI
                // New Category
                TableRow newTableRow = createTableRowForItemCategory("New_Category", table_Categories);
                table_Categories.addView(newTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // New Values
                TableRow newValueTableRow = new TableRow(CategoryActivity.this);
                newValueTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                for (int i = 0; i < itemList.size(); i++) {
                    TextView newValueTextView = createTextViewForItemValue(itemList.get(i).getItemID(), "New Value", data.getColumnName(data.getColumnCount() - 1), data.getColumnCount() - 1);
                    newValueTableRow.addView(newValueTextView);
                }
                table_Items.addView(newValueTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // 3. into itemList
                for (int i = 0; i < itemList.size(); i++) {
                    itemList.get(i).getItemValues().add("New Value");
                }

                // TODO: Remove ===========================================
                System.out.println("============= item List ==============");
                for (int i = 0; i < itemList.size(); i++) {
                    System.out.print(itemList.get(i).getItemID() + ". " + itemList.get(i).getItemName() + ", ");
                    for (int j = 0; j < itemList.get(i).getItemValues().size(); j++) {
                        System.out.print(itemList.get(i).getItemValues().get(j) + ", ");
                    }
                    System.out.println();
                }
                System.out.println("======================================");

                System.out.println("============= Database ===============");
                Cursor test_Data2 = mDatabaseHelper.getData(category_Name);
                while(test_Data2.moveToNext()) {
                    System.out.print(test_Data2.getString(0) + ". " + test_Data2.getString(1) + ", ");
                    for (int i = 2; i < test_Data2.getColumnCount(); i++) {
                        System.out.print(test_Data2.getColumnName(i) + ": " + test_Data2.getString(i) + ", ");
                    }
                    System.out.println();
                }
                System.out.println("======================================");
                // TODO: ==================================================
            }
        });
    }

    /*
    Function for creating TextView for item name
     */
    private TextView createTextViewForItemName(final String id, String itemName, final TableRow tableRow) {

        final TextView newItemTextView = new TextView(CategoryActivity.this);
        newItemTextView.setText(itemName);
        newItemTextView.setGravity(Gravity.CENTER);
        newItemTextView.setWidth(300);
        newItemTextView.setHeight(150);
        newItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(CategoryActivity.this).create();
                final EditText itemNameEditText = new EditText(CategoryActivity.this);

                dialog.setTitle("New Item Name: \n");
                dialog.setView(itemNameEditText);

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newItemName = itemNameEditText.getText().toString();

                        // Change on database
                        mDatabaseHelper.updateData(category_Name, id, "name", newItemName);

                        // Change on itemList
                        for (int i = 0; i < itemList.size(); i++) {
                            if (itemList.get(i).getItemID() == id) {
                                itemList.get(i).setItemName(newItemName);
                                break;
                            }
                        }

                        // Change on UI
                        newItemTextView.setText(newItemName);
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Delete itemName from the DB
                        mDatabaseHelper.deleteData(category_Name, id);

                        // Delete itemName from UI
                        int indexOfItemName = tableRow.indexOfChild(newItemTextView);
                        tableRow.removeView(newItemTextView);
                        itemList.remove(indexOfItemName);
                        for (int i = 0; i < itemList.size(); i++) {
                            TableRow curr_Row = (TableRow) table_Items.getChildAt(i);
                            curr_Row.removeView(curr_Row.getChildAt(indexOfItemName));
                        }
                    }
                });

                dialog.show();
            }
        });

        return newItemTextView;
    }

    /*
    Function for creating TableRow for item category
     */
    private TableRow createTableRowForItemCategory(final String categoryName, final TableLayout tableLayout) {

        final TableRow newTableRow = new TableRow(CategoryActivity.this);
        newTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        final TextView newTextView = new TextView(CategoryActivity.this);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newTextView.setHeight(100);
        newTextView.setText(categoryName);
        newTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(CategoryActivity.this).create();
                final EditText itemCategoryEditText = new EditText(CategoryActivity.this);

                dialog.setTitle("New Item Category: \n");
                dialog.setView(itemCategoryEditText);

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItemCategory = itemCategoryEditText.getText().toString();

                        // Change on DB
                        mDatabaseHelper.renameColumn(category_Name, categoryName, newItemCategory);

                        // Change on UI
                        newTextView.setText(newItemCategory);

                        System.out.println("============= Database ===============");
                        Cursor test_Data = mDatabaseHelper.getData(category_Name);
                        while(test_Data.moveToNext()) {
                            System.out.print(test_Data.getString(0) + ". " + test_Data.getString(1) + ", ");
                            for (int i = 2; i < test_Data.getColumnCount(); i++) {
                                System.out.print(test_Data.getColumnName(2) + ": " + test_Data.getString(2) + ", ");
                            }
                            System.out.println();
                        }
                        System.out.println("======================================");
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete itemCategory from the DB
                        mDatabaseHelper.deleteColumn(category_Name, categoryName);

                        // Delete itemName from UI
                        int indexOfCategory = table_Categories.indexOfChild(newTableRow);
                        table_Categories.removeView(newTableRow);
                        table_Items.removeView(table_Items.getChildAt(indexOfCategory));
                        for (int i = 0; i < itemList.size(); i++) {
                            itemList.get(i).getItemValues().remove(indexOfCategory);
                        }
                    }
                });

                dialog.show();
            }
        });

        newTableRow.addView(newTextView);

        return newTableRow;
    }

    /*
    Method for creating TextView for item Values
     */
    private TextView createTextViewForItemValue(final String id, final String itemValue, final String category, final int category_Index) {
        final TextView newItemValueTextView = new TextView(CategoryActivity.this);
        newItemValueTextView.setGravity(Gravity.CENTER);
        newItemValueTextView.setWidth(300);
        newItemValueTextView.setHeight(100);
        newItemValueTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        newItemValueTextView.setText(itemValue);
        newItemValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(CategoryActivity.this).create();
                final EditText itemValueEditText = new EditText(CategoryActivity.this);

                dialog.setTitle("New Item Value: \n");
                dialog.setView(itemValueEditText);

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItemValue = itemValueEditText.getText().toString();

                        // Change on DB
                        mDatabaseHelper.updateData(category_Name, id, category, newItemValue);

                        // Change on itemList
                        for (int i = 0; i < itemList.size(); i++) {
                            if (itemList.get(i).getItemID() == id) {
                                itemList.get(i).setItemValue(category_Index, newItemValue);
                                break;
                            }
                        }

                        // Change on UI
                        newItemValueTextView.setText(newItemValue);
                    }
                });

                dialog.show();
            }
        });

        return newItemValueTextView;
    }
}
