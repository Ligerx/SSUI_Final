package com.example.admin.ssuifinalproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.ssuifinalproject.BeatData;
import com.example.admin.ssuifinalproject.Database.Models.BPM;
import com.example.admin.ssuifinalproject.Database.Models.PracticeRun;
import com.example.admin.ssuifinalproject.Database.Models.Song;

import java.util.ArrayList;
import java.util.Date;

/**
 * Basically the schema for the SQLite database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 3; // bumped for missing file path, then to reset bad ids
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
    private static final String KEY_FILE_PATH = "file_path";
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
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_TITLE + " TEXT,"
                + KEY_TARGET_BPM + " REAL,"
                + KEY_CREATED_AT + " INTEGER" + ")";

    // Practice_run table create statement
    private static final String CREATE_TABLE_PRACTICE_RUN =
            "CREATE TABLE " + TABLE_PRACTICE_RUN + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_SONG_ID + " INTEGER,"
                    + KEY_FILE_PATH + " TEXT,"
                    + KEY_PR_TARGET_BPM + " REAL,"
                    + KEY_MEDIAN_BPM + " REAL,"
                    + KEY_CREATED_AT + " INTEGER" + ")";

    // BPM table create statement
    private static final String CREATE_TABLE_BPM =
            "CREATE TABLE " + TABLE_BPM + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
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





    //////// CRUD type methods n stuff ////////

    //// Song CRUD
    public long createSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();

        int time = (int)System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle());
        values.put(KEY_TARGET_BPM, song.getTargetBPM());
        values.put(KEY_CREATED_AT, time);

        long id = db.insert(TABLE_SONG, null, values);

        song.setId((int) id);
        song.setCreatedAt(new Date(time));
        return id;
    }

    public Song getSong(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE "
                + KEY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Song song = new Song(c.getInt(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getDouble(c.getColumnIndex(KEY_TARGET_BPM)),
                c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

        return song;
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONG
                + " ORDER BY " + KEY_TITLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Song song = new Song(c.getInt(c.getColumnIndex(KEY_ID)),
                        c.getString(c.getColumnIndex(KEY_TITLE)),
                        c.getDouble(c.getColumnIndex(KEY_TARGET_BPM)),
                        c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

                songs.add(song);
            }
            while (c.moveToNext());
        }

        return songs;
    }

    public int updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle());
        values.put(KEY_TARGET_BPM, song.getTargetBPM());
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        return db.update(TABLE_SONG, values, KEY_ID + " = ?",
                new String[] { String.valueOf(song.getId()) });
    }



    //// PracticeRun CRUD
    public long createPracticeRun(PracticeRun practiceRun) {
        SQLiteDatabase db = this.getWritableDatabase();

        int time = (int)System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_SONG_ID, practiceRun.getSong_id());
        values.put(KEY_FILE_PATH, practiceRun.getFilePath());
        values.put(KEY_TARGET_BPM, practiceRun.getTargetBPM());
        values.put(KEY_MEDIAN_BPM, practiceRun.getMedianBPM());
        values.put(KEY_CREATED_AT, time);

        long practiceRun_id = db.insert(TABLE_PRACTICE_RUN, null, values);

        // also insert all the bpms
        for(double timing : practiceRun.getBeatData().getBeatTiming()) {
            BPM bpm = new BPM((int) practiceRun_id, timing, (int) System.currentTimeMillis());
            createBPM(bpm); // insert into the database
        }

        practiceRun.setId((int) practiceRun_id);
        practiceRun.setCreatedAt(new Date(time));
        return practiceRun_id;
    }

    public PracticeRun getPracticeRun(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PRACTICE_RUN + " WHERE "
                + KEY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        PracticeRun practiceRun = new PracticeRun(c.getInt(c.getColumnIndex(KEY_ID)),
                c.getInt(c.getColumnIndex(KEY_SONG_ID)),
                c.getDouble(c.getColumnIndex(KEY_PR_TARGET_BPM)),
                c.getDouble(c.getColumnIndex(KEY_MEDIAN_BPM)),
                c.getInt(c.getColumnIndex(KEY_CREATED_AT)),
                getBeatDataForPracticeRun(id)); // beat data

        practiceRun.setFilePath(c.getString(c.getColumnIndex((KEY_FILE_PATH)))); // was tacked on later cause I forgot

        return practiceRun;
    }

    public ArrayList<PracticeRun> getAllPracticeRuns() {
        ArrayList<PracticeRun> practiceRuns = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRACTICE_RUN
                + " ORDER BY " + KEY_CREATED_AT + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PracticeRun practiceRun = new PracticeRun(c.getInt(c.getColumnIndex(KEY_ID)),
                        c.getInt(c.getColumnIndex(KEY_SONG_ID)),
                        c.getDouble(c.getColumnIndex(KEY_PR_TARGET_BPM)),
                        c.getDouble(c.getColumnIndex(KEY_MEDIAN_BPM)),
                        c.getInt(c.getColumnIndex(KEY_CREATED_AT)),
                        getBeatDataForPracticeRun(c.getColumnIndex(KEY_ID))); // beat data from record id

                practiceRun.setFilePath(c.getString(c.getColumnIndex((KEY_FILE_PATH)))); // tacked on cause I forgot

                practiceRuns.add(practiceRun);
            }
            while (c.moveToNext());
        }

        return practiceRuns;
    }

    public ArrayList<PracticeRun> findAllPracticeRunsBySong(int song_id) {
        ArrayList<PracticeRun> practiceRuns = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRACTICE_RUN + " WHERE "
                + KEY_SONG_ID + " = " + song_id
                + " ORDER BY " + KEY_CREATED_AT + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PracticeRun practiceRun = new PracticeRun(c.getInt(c.getColumnIndex(KEY_ID)),
                        c.getInt(c.getColumnIndex(KEY_SONG_ID)),
                        c.getDouble(c.getColumnIndex(KEY_PR_TARGET_BPM)),
                        c.getDouble(c.getColumnIndex(KEY_MEDIAN_BPM)),
                        c.getInt(c.getColumnIndex(KEY_CREATED_AT)),
                        getBeatDataForPracticeRun(c.getColumnIndex(KEY_ID))); // beat data from record id

                practiceRun.setFilePath(c.getString(c.getColumnIndex((KEY_FILE_PATH)))); // tacked on cause I forgot

                practiceRuns.add(practiceRun);
            }
            while (c.moveToNext());
        }

        return practiceRuns;
    }

    // Create BeatData from bpm data in the db
    private BeatData getBeatDataForPracticeRun(int practiceRun_id) {
        ArrayList<BPM> bpms = findAllBPMByPracticeRun(practiceRun_id);
        ArrayList<Double> timings = new ArrayList<>();

        for(BPM bpm : bpms) {
            timings.add(bpm.getTime());
        }

        return new BeatData(timings);
    }

    public int updatePracticeRun(PracticeRun practiceRun) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILE_PATH, practiceRun.getFilePath());
        values.put(KEY_SONG_ID, practiceRun.getSong_id());
        values.put(KEY_PR_TARGET_BPM, practiceRun.getTargetBPM());
        values.put(KEY_MEDIAN_BPM, practiceRun.getMedianBPM());
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        return db.update(TABLE_PRACTICE_RUN, values, KEY_ID + " = ?",
                new String[] { String.valueOf(practiceRun.getId()) });
    }



    //// BPM CRUD
    public long createBPM(BPM bpm) {
        SQLiteDatabase db = this.getWritableDatabase();

        int time = (int)System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_PRACTICE_RUN_ID, bpm.getPracticeRun_id());
        values.put(KEY_TIME, bpm.getTime());
        values.put(KEY_CREATED_AT, time);

        long id = db.insert(TABLE_BPM, null, values);

        bpm.setId((int) id);
        bpm.setCreatedAt(new Date(time));
        return id;
    }

    public BPM getBPM(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BPM + " WHERE "
                + KEY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        BPM bpm = new BPM(c.getInt(c.getColumnIndex(KEY_ID)),
                c.getInt(c.getColumnIndex(KEY_PRACTICE_RUN_ID)),
                c.getDouble(c.getColumnIndex(KEY_TIME)),
                c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

        return bpm;
    }

    public ArrayList<BPM> findAllBPMByPracticeRun(int practiceRun_id) {
        ArrayList<BPM> bpms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BPM + " WHERE "
                + KEY_PRACTICE_RUN_ID + " = " + practiceRun_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                BPM bpm = new BPM(c.getInt(c.getColumnIndex(KEY_ID)),
                        c.getInt(c.getColumnIndex(KEY_PRACTICE_RUN_ID)),
                        c.getDouble(c.getColumnIndex(KEY_TIME)),
                        c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

                bpms.add(bpm);
            }
            while (c.moveToNext());
        }

        return bpms;
    }
}
