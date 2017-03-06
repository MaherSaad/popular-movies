package com.example.mahertag.movieapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mahertag.movieapp.Data.MovieData.MovieTable;
/**
 * Created by Mahertag on 3/3/2017.
 */

public class MovieDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";

    private static final int DB_VERSION = 1;

    public MovieDB(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE =

                "CREATE TABLE " + MovieTable.TABLE_NAME + " (" +


                        MovieTable._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieTable.COLUMN_MOVIE_ID       + " TEXT, "                          +

                        MovieTable.COLUMN_MOVIE_NAME + " TEXT,"                               +

                        MovieTable.COLUMN_MOVIE_RELASE_DATE   + " TEXT, "                     +

                        MovieTable.COLUMN_MOVIE_RATE   + " TEXT, "                            +

                        MovieTable.COLUMN_MOVIE_OVERVIEW   + " TEXT, "                        +

                        MovieTable.COLUMN_MOVIE_POSTER   + " TEXT, "                          +

                        MovieTable.COLUMN_MOVIE_BACKDROP + " TEXT);";

        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +MovieTable.TABLE_NAME);
        onCreate(db);
    }
}
