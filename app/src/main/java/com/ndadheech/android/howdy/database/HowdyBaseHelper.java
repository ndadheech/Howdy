package com.ndadheech.android.howdy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ndadheech.android.howdy.database.HowdyDbSchema.HowdyTable;

public class HowdyBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "howdyBase.db";
    public static final String TAG = "HowdyBaseHelper";

    public HowdyBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating a new Howdy database");
        db.execSQL("create table " + HowdyTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            HowdyTable.Cols.UUID + ", " +
            HowdyTable.Cols.NAME + ", " +
            HowdyTable.Cols.DATE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
