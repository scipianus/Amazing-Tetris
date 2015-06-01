package com.indysoft.amazingtetris;

/**
 * Created by ciprian on 6/1/15.
 */

import android.database.sqlite.SQLiteDatabase;

public class HighScoresDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";
    public static final String KEY_DIFFICULTY = "difficulty";
    public static final String KEY_NUMROWS = "numrows";
    public static final String KEY_NUMCOLUMNS = "numcolumns";
    public static final String KEY_SPEED = "speed";

    public static final String SQLITE_TABLE = "HighScoresTable";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY autoincrement," +
                    KEY_NAME + " TEXT," +
                    KEY_SCORE + " INTEGER," +
                    KEY_DIFFICULTY + " TEXT," +
                    KEY_NUMROWS + " TEXT," +
                    KEY_NUMCOLUMNS + " TEXT," +
                    KEY_SPEED + " TEXT);";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

}