package com.example.versus;

import java.util.ArrayList;

public class VersusItem {
    private String item_ID;
    private String item_Name;
    private ArrayList<String> item_Category;
    private ArrayList<String> item_Value;

    public VersusItem(String name) {
        item_Name = name;
    }

    public String getItemName() {
        return item_Name;
    }

    public void setItemName(String name) {
        item_Name = name;
    }
}
