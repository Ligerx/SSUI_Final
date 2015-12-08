package com.example.admin.ssuifinalproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Basically the schema for the SQLite database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "KeepTheBeats"; // beats because angel beats reference

    // Table Names
    private static final String TABLE_SONG = "songs";
    private static final String TABLE_PRACTICE_RUN = "practice_runs";
    private static final String TABLE_BPM = "bpm";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // SONG Table - column names
    private static final String KEY_TITLE = "title";
    private static final String KEY_TARGET_BPM = "target_bpm";

    // PRACTICE_RUN Table - column names
    private static final String KEY_SONG_ID = "song_id";
    private static final String KEY_PR_TARGET_BPM = "target_bpm"; // conflicting variable name, had to rename
    private static final String KEY_MEDIAN_BPM = "median_bpm";

    // BPM Table - column names
    private static final String KEY_PRACTICE_RUN_ID = "practice_run_id";
    private static final String KEY_TIME = "time";

    //// Table Create Statements
    // Note: created_at time is stored as ints for currentTimeMillis()

    // Song table create statement
    private static final String CREATE_TABLE_SONG =
            "CREATE TABLE " + TABLE_SONG + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_TARGET_BPM + " REAL,"
                + KEY_CREATED_AT + " INTEGER" + ")";

    // Practice_run table create statement
    private static final String CREATE_TABLE_PRACTICE_RUN =
            "CREATE TABLE " + TABLE_PRACTICE_RUN + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_SONG_ID + " INTEGER,"
                    + KEY_PR_TARGET_BPM + " REAL,"
                    + KEY_MEDIAN_BPM + " REAL,"
                    + KEY_CREATED_AT + " INTEGER" + ")";

    // BPM table create statement
    private static final String CREATE_TABLE_BPM =
            "CREATE TABLE " + TABLE_BPM + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_PRACTICE_RUN_ID + " INTEGER,"
                    + KEY_TIME + " REAL,"
                    + KEY_CREATED_AT + " INTEGER" + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, "DatabaseHelper creating database");

        // creating required tables
        db.execSQL(CREATE_TABLE_SONG);
        db.execSQL(CREATE_TABLE_PRACTICE_RUN);
        db.execSQL(CREATE_TABLE_BPM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG, "DatabaseHelper upgrading/dropping database");

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRACTICE_RUN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BPM);

        // create new tables
        onCreate(db);
    }
}
