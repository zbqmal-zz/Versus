package com.example.versus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME_TYPE = "type_table";
    private static final String TYPE_NAME = "name";

    private static final String ITEM_NAME = "name";
    private static final String ITEM_CATEGORY = "New_Category";
    private static final String CATEGORY_COUNT = "Count";

    public DatabaseHelper(Context context) { super(context, TABLE_NAME_TYPE, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTypeTable = "CREATE TABLE " + TABLE_NAME_TYPE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + TYPE_NAME + " TEXT)";
        db.execSQL(createTypeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_NAME_TYPE);
        onCreate(db);
    }

    /*
    Method for creating an item table
     */
    public void addTable(String table_Name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Data Table
        String createItemTable = "CREATE TABLE '" + table_Name + "' (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME + " TEXT, '" + ITEM_CATEGORY + "' TEXT DEFAULT 'New Value')";
        db.execSQL(createItemTable);

        // Count Table
        String createCountTable = "CREATE TABLE " + table_Name + "_Count (ID INTEGER PRIMARY KEY AUTOINCREMENT, '" + CATEGORY_COUNT + "' INT DEFAULT 0)";
        db.execSQL(createCountTable);
    }

    /*
    Method for adding a raw data into a table
     */
    public boolean addData(String table_Name, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, name);

        long result = db.insert("'" + table_Name + "'", null, contentValues);
        // if data as inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
    Method for adding a data
     */
    public boolean addCountData(String table_Name, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Count", data);

        long result = db.insert("'" + table_Name + "'", null, contentValues);
        // if data as inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
    Method for adding an item data into item table
     */
    public boolean addItemData(String table_Name, String newItemName, String newItemValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor data = getData(table_Name);
        contentValues.put(ITEM_NAME, newItemName);

        for (int i = 2; i < data.getColumnCount(); i++) {
            contentValues.put(data.getColumnName(i), newItemValue);
        }

        long result = db.insert("'" + table_Name + "'", null, contentValues);
        // if data as inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
    Method for adding a column into item table
     */
    public void addColumn(String table_Name, String columnName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String createColumn = "ALTER TABLE '" + table_Name + "' ADD COLUMN " + columnName + " TEXT DEFAULT 'New Value'";
        db.execSQL(createColumn);
    }

    /*
    Method for updating data
     */
    public boolean updateData(String table_Name, String id, String category, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put(category, value);
        db.update("'" + table_Name + "'", contentValues, "ID = ?", new String[] { id });
        return true;
    }

    /*
    Method for getting data from a table
     */
    public Cursor getData(String table_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM '" + table_Name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteData(String table_Name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteData = "DELETE FROM '" + table_Name + "' WHERE ID = " + id;
        db.execSQL(deleteData);
    }

    /*
    Method for renaming table
     */
    public void renameTable(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String renameTable = "ALTER TABLE '" + oldName + "' RENAME TO '" + newName + "'";
        db.execSQL(renameTable);
    }

    /*
    Method for deleting a table from db
     */
    public void deleteTable(String table_Name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop table
        String dropTable = "DROP TABLE IF EXISTS '" + table_Name + "'";
        db.execSQL(dropTable);
    }

    /*
    Method for renaming a column
     */
    public void renameColumn(String table_Name, String old_Column_Name, String new_Column_Name) {

        // Retrieve the data from old table
        Cursor data = getData(table_Name);

        // Create new table
        SQLiteDatabase db = this.getWritableDatabase();
        String tempTableName = "temp_table";
        String createNewTable = "CREATE TABLE " + tempTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME + " TEXT,";
        data.moveToNext();
        for (int i = 2; i < data.getColumnCount(); i++) {
            System.out.println("old_Column_Name: " + old_Column_Name + ", new_Column_Name: " + new_Column_Name + ", data.getColumnName(" + i + "): " + data.getColumnName(i));
            if (old_Column_Name.equals(data.getColumnName(i))) {
                createNewTable += (" " + new_Column_Name + " TEXT");
                System.out.println("Changed!");
            } else {
                createNewTable += (" " + data.getColumnName(i) + " TEXT");
                System.out.println("Not Changed!");
            }

            if (i < data.getColumnCount() - 1) {
                createNewTable += ",";
            } else {
                createNewTable += ")";
            }
        }
        db.execSQL(createNewTable);

        // Copy and paste old table into new table
        data = getData(table_Name);
        while (data.moveToNext()) {

            // Put each value into contentValues
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", Integer.parseInt(data.getString(0)));
            for (int i = 1; i < data.getColumnCount(); i++) {
                if (old_Column_Name.equals(data.getColumnName(i))) {
                    contentValues.put(new_Column_Name, data.getString(i));
                } else {
                    contentValues.put(data.getColumnName(i), data.getString(i));
                }
            }

            // Add each value
            db.insert(tempTableName, null, contentValues);
        }

        // Delete the old table
        deleteTable(table_Name);

        // Rename new table as the old name
        renameTable(tempTableName, table_Name);
    }

    /*
    Method for deleting a column
     */
    public void deleteColumn(String table_Name, String column_Name) {
        // Retrieve the data from old table
        Cursor data = getData(table_Name);

        // Create new table
        SQLiteDatabase db = this.getWritableDatabase();
        String tempTableName = "temp_table";
        String createNewTable = "CREATE TABLE " + tempTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME + " TEXT";
        data.moveToNext();
        for (int i = 2; i < data.getColumnCount(); i++) {
            if (!column_Name.equals(data.getColumnName(i))) {
                createNewTable += (", " + data.getColumnName(i) + " TEXT");
            }
        }
        createNewTable += ")";
        db.execSQL(createNewTable);

        // Copy and paste old table into new table
        data = getData(table_Name);
        while (data.moveToNext()) {

            // Put each value into contentValues
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", Integer.parseInt(data.getString(0)));
            for (int i = 1; i < data.getColumnCount(); i++) {
                if (!column_Name.equals(data.getColumnName(i))) {
                    contentValues.put(data.getColumnName(i), data.getString(i));
                }
            }

            // Add each value
            db.insert(tempTableName, null, contentValues);
        }

        // Delete the old table
        deleteTable(table_Name);

        // Rename new table as the old name
        renameTable(tempTableName, table_Name);
    }
}
