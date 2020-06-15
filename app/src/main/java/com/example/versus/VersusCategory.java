package com.example.versus;

public class VersusCategory {

    private String category_ID;
    private String category_Name;

    public VersusCategory(String id, String name) {
        category_ID = id;
        category_Name = name;
    }

    public String getCategoryID() { return category_ID; }
    public String getCategoryName() {
        return category_Name;
    }

    public void setCategoryID(String id) { category_ID = id; }
    public void setCategoryName(String name) {
        category_Name = name;
    }
}
