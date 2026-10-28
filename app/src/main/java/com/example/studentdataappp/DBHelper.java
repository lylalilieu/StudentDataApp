package com.example.studentdataappp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "student";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "student_id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean addStudent(String id, String name, int age) {
        ContentValues values = new ContentValues();
        values.put("student_id", id);
        values.put("name", name);
        values.put("age", age);
        long result = getWritableDatabase().insert(TABLE, null, values);
        return result != -1;
    }

    public boolean editStudent(String id, String name, int age) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        int rows = getWritableDatabase().update(TABLE, values, "student_id = ?", new String[]{id});
        return rows > 0;
    }

    public boolean deleteStudent(String id) {
        int rows = getWritableDatabase().delete(TABLE, "student_id = ?", new String[]{id});
        return rows > 0;
    }

    public Cursor getAllStudents() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE, null);
    }
}