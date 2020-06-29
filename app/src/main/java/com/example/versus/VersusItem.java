package com.example.versus;

import java.util.ArrayList;

public class VersusItem {
    private String item_ID;
    private String item_Name;
//    private ArrayList<String> item_Category;
    private ArrayList<String> item_Values;

    public VersusItem(String id, String name) {
        item_ID = id;
        item_Name = name;

        item_Values = new ArrayList<>();
//        item_Values.add("New Value");
    }

    public String getItemName() {
        return item_Name;
    }
    public String getItemID() { return item_ID; }
    public ArrayList<String> getItemValues() { return item_Values; }

    public void setItemName(String name) {
        item_Name = name;
    }
    public void setItemID(String newItemID) { item_ID = newItemID; }
    public void setItemValue(int index, String newValue) {
        item_Values.set(index, newValue);
    }
}
