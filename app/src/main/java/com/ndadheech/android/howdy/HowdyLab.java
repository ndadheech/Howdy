package com.ndadheech.android.howdy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ndadheech.android.howdy.database.HowdyBaseHelper;
import com.ndadheech.android.howdy.database.HowdyDbSchema.HowdyTable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class HowdyLab {
    private static HowdyLab sHowdyLab;
    private SQLiteDatabase mDatabase;
    public static final String TAG = "HowdyLab";

    public static HowdyLab get(Context context) {
        if (sHowdyLab == null) {
            sHowdyLab = new HowdyLab(context);
        }
        return sHowdyLab;
    }

    private HowdyLab(Context context) {
        mDatabase = new HowdyBaseHelper(context.getApplicationContext())
            .getWritableDatabase();
    }

    public void addHowdy(Howdy howdy) {
        ContentValues values = getContentValues(howdy);
        mDatabase.insert(HowdyTable.NAME, null, values);
    }

    public String getLatestHowdy(String latestHowdyFormat, String dateFormat) {
        Log.d(TAG, "Querying the Howdy db for last Howdy sent");
        HowdyCursorWrapper cursor = queryHowdies();
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLatestHowdy(latestHowdyFormat, dateFormat);
        } finally {
            cursor.close();
        }
    }

    private HowdyCursorWrapper queryHowdies() {
        Cursor cursor = mDatabase.query(
            HowdyTable.NAME,
            null,
            null, // where clause
            null, // where args
            null, // groupBy
            null, //having
            HowdyTable.Cols.DATE + " DESC",
            "1"
        );
        return new HowdyCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Howdy howdy) {
        ContentValues values = new ContentValues();
        values.put(HowdyTable.Cols.UUID, howdy.getId().toString());
        values.put(HowdyTable.Cols.NAME, howdy.getFriend().getFriendName());
        values.put(HowdyTable.Cols.DATE, howdy.getDate().getTime());
        return values;
    }

    private class HowdyCursorWrapper extends CursorWrapper {
        public HowdyCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public String getLatestHowdy(String latestHowdyFormat, String dateFormat) {
            DateTime dateTime = new DateTime(getLong(getColumnIndex(HowdyTable.Cols.DATE)));
            DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormat);
            String dateString = fmt.print(dateTime);
            String friendName = getString(getColumnIndex(HowdyTable.Cols.NAME));
            return String.format(latestHowdyFormat, friendName, dateString);
        }
    }
}
