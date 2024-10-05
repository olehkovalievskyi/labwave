package com.kovalievskyi.labwave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private static final String db_name = "labwave";
    private static final int db_version = 1;

    private static final String db_table = "tasks";
    private static final String tasks_colum_name = "name";
    private static final String tasks_colum_deadline = "deadline";
    private static final String tasks_colum_active = "active";

    public DataBase(@Nullable Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TIMESTAMP NULL, %s BOOLEAN DEFAULT TRUE);",
                db_table, tasks_colum_name, tasks_colum_deadline, tasks_colum_active);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DELETE TABLES IF EXISTS %s", db_table));
        onCreate(db);
    }

    public void addTask(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tasks_colum_name, name);
        values.put(tasks_colum_active, true);

        db.insertWithOnConflict(db_table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void delTask(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(db_table, db_table + " = ?", new String[] {name});
        db.close();
    }

    public ArrayList<String> getAllTasks(){
        ArrayList<String> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(db_table, new String[]{tasks_colum_name},
                null, null, null, null, null);

        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(tasks_colum_name);
            tasks.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
