package com.jvil723.eventforce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "event.db";
    private static final String TABLE_NAME = "event_table";
    private static final String COL1 = "EVENT_ID";
    private static final String COL2 = "EVENT_NAME";
    private static final String COL3 = "EVENT_DATE";
    private static final String COL4 = "EVENT_TIME";
    private static final String COL5 = "EVENT_DESCRIPTION";
    private static final String COL6 = "EVENT_TYPE";

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EVENT_NAME TEXT, " +
                "EVENT_DATE TEXT, " +
                "EVENT_TIME TEXT, " +
                "EVENT_DESCRIPTION TEXT, " +
                "EVENT_TYPE TEXT)"); // Create the new column
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Create
    public boolean addEvent(String eventName, String eventDate, String eventTime, String eventDescription, String eventType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, eventName);
        contentValues.put(COL3, eventDate);
        contentValues.put(COL4, eventTime);
        contentValues.put(COL5, eventDescription);
        contentValues.put(COL6, eventType);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Read
    public Cursor getAllEvents(String eventType) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL6 + " = ?", new String[]{eventType});
    }

    public Cursor getEventById(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = ?", new String[]{String.valueOf(eventId)});
    }

    // Update
    public boolean updateEvent(int eventId, String eventName, String eventDate, String eventTime, String eventDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, eventName);
        contentValues.put(COL3, eventDate);
        contentValues.put(COL4, eventTime);
        contentValues.put(COL5, eventDescription);
        int result = db.update(TABLE_NAME, contentValues, COL1 + " = ?", new String[]{String.valueOf(eventId)});
        return result > 0;
    }

    // Delete
    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL1 + " = ?", new String[]{String.valueOf(eventId)});
        return result > 0;
    }
}
