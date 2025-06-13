package com.jvil723.eventforce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME = "user_table";
    private static final String COL1 = "PERSONAL_USERNAME";
    private static final String COL2 = "PERSONAL_PASSWORD";
    private static final String COL3 = "STUDENT_USERNAME";
    private static final String COL4 = "STUDENT_PASSWORD";
    private static final String COL5 = "USER_EMAIL";
    private static final String COL6 = "PERSONAL_PHONE_NUMBER";
    private static final String COL7 = "STUDENT_PHONE_NUMBER";

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PERSONAL_USERNAME TEXT UNIQUE, " +
                "PERSONAL_PASSWORD TEXT, " +
                "STUDENT_USERNAME TEXT UNIQUE, " +
                "STUDENT_PASSWORD TEXT, " +
                "USER_EMAIL TEXT" +
                "PERSONAL_PHONE_NUMBER, " +
                "STUDENT_PHONE_NUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add personal user to database
    public boolean addPersonalUser(String personalUsername, String personalPassword, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, personalUsername);
        contentValues.put(COL2, personalPassword);
        contentValues.put(COL5, userEmail);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Add student user to database
    public boolean addStudentUser(String studentUsername, String studentPassword, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3, studentUsername);
        contentValues.put(COL4, studentPassword);
        contentValues.put(COL5, userEmail);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Check if personal user exists in database
    public boolean checkPersonalUser(String personalUsername, String personalPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE PERSONAL_USERNAME = ? AND PERSONAL_PASSWORD = ?", new String[]{personalUsername, personalPassword});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Check if student user exists in database
    public boolean checkStudentUser(String studentUsername, String studentPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE STUDENT_USERNAME = ? AND STUDENT_PASSWORD = ?", new String[]{studentUsername, studentPassword});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Save phone number to database
    public boolean savePhoneNumber(String phoneNumber, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (userType.equals("Personal")) {
            contentValues.put(COL6, phoneNumber);
        } else {
            contentValues.put(COL7, phoneNumber);
        }
        long result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{"1"});
        return result != -1;
    }

    // Get phone number from database
    public String getPhoneNumber(String userType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (userType.equals("Personal")) {
            cursor = db.rawQuery("SELECT PERSONAL_PHONE_NUMBER FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{"1"});
        } else {
            cursor = db.rawQuery("SELECT STUDENT_PHONE_NUMBER FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{"1"});
        }
        if (cursor.moveToFirst()) {
            String phoneNumber = cursor.getString(0);
            cursor.close();
            return phoneNumber;
        } else {
            cursor.close();
            return null;
        }
    }
}
