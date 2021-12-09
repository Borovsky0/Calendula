package com.bvl.calendula;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "calendula.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DAY_REPEAT = "day_repeat";
    private static final String COLUMN_WEEK_REPEAT = "week_repeat";
    private static final String COLUMN_TIME_START = "time_start";
    private static final String COLUMN_TIME_FINISH = "time_finish";
    private static final String COLUMN_TAGS = "tags";
    private static final String COLUMN_TEXT_NOTE = "text_note";
    private static final String COLUMN_PIC_NOTE = "pic_note";
    private static final String COLUMN_AUDIO_NOTE = "audio_note";

    private static final String WEEKLY = "0";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_DAY_REPEAT + " TEXT, " + COLUMN_WEEK_REPEAT + " TEXT, " +
                COLUMN_TIME_START + " TEXT, " + COLUMN_TIME_FINISH + " TEXT, " + COLUMN_TAGS + " TEXT, " + COLUMN_TEXT_NOTE + " TEXT, " +
                COLUMN_PIC_NOTE + " TEXT, " + COLUMN_AUDIO_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void add(String name, String date, String day_of_week,
             String periodicity, String time_start, String time_finish,
             String tags, String text_note, String pic_note, String audio_note){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_DAY_REPEAT, day_of_week);
        cv.put(COLUMN_WEEK_REPEAT, periodicity);
        cv.put(COLUMN_TIME_START, time_start);
        cv.put(COLUMN_TIME_FINISH, time_finish);
        cv.put(COLUMN_TAGS, tags);
        cv.put(COLUMN_TEXT_NOTE, text_note);
        cv.put(COLUMN_PIC_NOTE, pic_note);
        cv.put(COLUMN_AUDIO_NOTE, audio_note);

        db.insert(TABLE_NAME, null, cv);
    }

    public Cursor read(Calendar date){
        String fDate = new SimpleDateFormat("yyyy/MM/dd").format(date.getTime());
        String dayRepeat = Integer.toString(date.get(Calendar.DAY_OF_WEEK));
        String weekRepeat =  Integer.toString(date.get(Calendar.WEEK_OF_YEAR) % 2 == 0 ? 2 : 1);
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE (" + COLUMN_DATE + " = '" + fDate + "' AND " + COLUMN_WEEK_REPEAT +
                " = 'NULL')" + " OR (" + COLUMN_DAY_REPEAT
                + " = '" + dayRepeat + "' AND (" + COLUMN_WEEK_REPEAT + " = '" + weekRepeat + "' OR " + COLUMN_WEEK_REPEAT
                + " = '" + WEEKLY + "')) ORDER BY " + COLUMN_TIME_START + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void update(String id, String name, String date, String day_of_week,
                       String periodicity, String time_start, String time_finish,
                       String tags, String text_note, String pic_note, String audio_note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_DAY_REPEAT, day_of_week);
        cv.put(COLUMN_WEEK_REPEAT, periodicity);
        cv.put(COLUMN_TIME_START, time_start);
        cv.put(COLUMN_TIME_FINISH, time_finish);
        cv.put(COLUMN_TAGS, tags);
        cv.put(COLUMN_TEXT_NOTE, text_note);
        cv.put(COLUMN_PIC_NOTE, pic_note);
        cv.put(COLUMN_AUDIO_NOTE, audio_note);

        db.update(TABLE_NAME, cv, "id=?", new String[]{id});
    }

    public void delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{id});
    }
}