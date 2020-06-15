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

    private static final String TABLE_NAME_ITEM = "item_table";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_CATEGORY = "category_1";

    public DatabaseHelper(Context context) { super(context, TABLE_NAME_TYPE, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME_TYPE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + TYPE_NAME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_NAME_TYPE);
        onCreate(db);
    }

    public boolean addData(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, type);

        long result = db.insert(TABLE_NAME_TYPE, null, contentValues);
        // if data as inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String table_Name, String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("name", value);
        db.update(table_Name, contentValues, "ID = ?", new String[] { id });
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TYPE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int deleteData(String table_Name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_Name, "ID = ?", new String[] { id });
    }
}
