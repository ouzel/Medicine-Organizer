package com.example.myapplication.med;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 11;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String MED = "med";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MED + " TEXT, "
            + DATE + " TEXT, " + AMOUNT + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(MedModel med) {
        ContentValues cv = new ContentValues();
        cv.put(MED, med.getMed());
        cv.put(DATE, med.getDate());
        cv.put(AMOUNT, med.getAmount());
        db.insert(TODO_TABLE, null, cv);

    }

    @SuppressLint("Range")
    public List<MedModel> getAllTasks() {
        List<MedModel> medList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null,
                    null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        MedModel med = new MedModel();
                        med.setId(cur.getInt(cur.getColumnIndex(ID)));
                        med.setMed(cur.getString(cur.getColumnIndex(MED)));
                        med.setAmount(cur.getInt(cur.getColumnIndex(AMOUNT)));
                        med.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        medList.add(med);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return medList;
    }

    public void updateTask(int id, String med, String date, int amount) {
        ContentValues cv = new ContentValues();
        cv.put(MED, med);
        cv.put(DATE, date);
        cv.put(AMOUNT, amount);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
    }
}

