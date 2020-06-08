package com.example.versus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements HorizontalScroll.ScrollViewListener, VerticalScroll.ScrollViewListener {

    private Context context;
    private ImageView btn_AddCol,
                      btn_AddRow;
    private TableLayout table_Row,
                        table_Items;
    private TableRow table_Col;
    private ArrayList<VersusItem> itemList;
    private ArrayList<String> categoryList;

    private HorizontalScroll itemName_Horizontal,
                             itemValue_Horizontal;

    private VerticalScroll itemCategory_Vertical,
                           itemValue_Vertical;

    private TextView init_ItemName, init_ItemValue, init_ItemCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Set up current category name
        Intent intent = getIntent();
        String category_Name = intent.getStringExtra("category_Name");
        TextView categoryNameTextView = findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category_Name);

        setupInitTextView();

        setupScrolling();
        itemName_Horizontal.setScrollViewListener(this);
        itemValue_Horizontal.setScrollViewListener(this);
        itemValue_Vertical.setScrollViewListener(this);
        itemCategory_Vertical.setScrollViewListener(this);

        createLists();
        setTables();
        setButtons();

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

    private void setupInitTextView() {
        init_ItemName = findViewById(R.id.textView_Itm);
        init_ItemValue = findViewById(R.id.textView_Value);
        init_ItemCategory = findViewById(R.id.textView_Category);

        init_ItemName.setOnClickListener(new View.OnClickListener() {
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
                        init_ItemName.setText(itemNameEditText.getText().toString());
                    }
                });

                dialog.show();
            }
        });

        init_ItemValue.setOnClickListener(new View.OnClickListener() {
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
                        init_ItemValue.setText(itemValueEditText.getText().toString());
                    }
                });

                dialog.show();
            }
        });

        init_ItemCategory.setOnClickListener(new View.OnClickListener() {
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
                        init_ItemCategory.setText(itemCategoryEditText.getText().toString());
                    }
                });

                dialog.show();
            }
        });
    }

    private void setupScrolling() {

        itemName_Horizontal = findViewById(R.id.itemName_Horizontal);
        itemValue_Horizontal = findViewById(R.id.itemValue_Horizontal);
        itemCategory_Vertical = findViewById(R.id.itemCategory_Vertical);
        itemValue_Vertical = findViewById(R.id.itemValue_Vertical);

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
                final TextView newItemTextView = new TextView(CategoryActivity.this);
                newItemTextView.setText("New Item");
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
                                newItemTextView.setText(itemNameEditText.getText().toString());
                            }
                        });

                        dialog.show();
                    }
                });
                table_Col.addView(newItemTextView);
                itemList.add(new VersusItem("New Item"));

                // For item values
                for (int i = 0; i < categoryList.size(); i++) {
                    // TextView for value
                    final TextView newValueTextView = new TextView(CategoryActivity.this);
                    newValueTextView.setGravity(Gravity.CENTER);
                    newValueTextView.setWidth(300);
                    newValueTextView.setHeight(100);
                    newValueTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    newValueTextView.setText("New Value");
                    newValueTextView.setOnClickListener(new View.OnClickListener() {
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
                                    newValueTextView.setText(itemValueEditText.getText().toString());
                                }
                            });

                            dialog.show();
                        }
                    });

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
                final TextView newTextView = new TextView(CategoryActivity.this);
                newTextView.setGravity(Gravity.CENTER);
                newTextView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                newTextView.setHeight(100);
                newTextView.setText("New Category");
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
                                newTextView.setText(itemCategoryEditText.getText().toString());
                            }
                        });

                        dialog.show();
                    }
                });

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
                    final TextView newValueTextView = new TextView(CategoryActivity.this);
                    newValueTextView.setGravity(Gravity.CENTER);
                    newValueTextView.setWidth(300);
                    newValueTextView.setHeight(100);
                    newValueTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    newValueTextView.setText("New Value");
                    newValueTextView.setOnClickListener(new View.OnClickListener() {
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
                                    newValueTextView.setText(itemValueEditText.getText().toString());
                                }
                            });

                            dialog.show();
                        }
                    });
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
