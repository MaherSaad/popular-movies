package com.example.mahertag.movieapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Mahertag on 3/3/2017.
 */

public class MovieProvider extends ContentProvider {

    private MovieDB  movieDB;

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieData.CONTENT_AUTHORITY,MovieData.MOVIE_PATH,MOVIE);
        uriMatcher.addURI(MovieData.CONTENT_AUTHORITY,MovieData.MOVIE_PATH + "/#",MOVIE_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {

        movieDB = new MovieDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = movieDB.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match){
            case MOVIE:
                cursor = database.query(MovieData.MovieTable.TABLE_NAME,
                        null,
                        null,
                        null,null,null,null);

                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor = database.query(MovieData.MovieTable.TABLE_NAME,
                        null,
                        "movie_id=?",
                        new String[]{id},
                        null,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase database = movieDB.getWritableDatabase();

        int res = sUriMatcher.match(uri);
        Uri returnedUri;
        switch (res){

            case MOVIE:

                long id = database.insert(MovieData.MovieTable.TABLE_NAME,null,values);

                if(id>0){
                    returnedUri = ContentUris.withAppendedId(MovieData.MovieTable.CONTENT_PROVIDER_URI,id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        SQLiteDatabase database = movieDB.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int res;
        switch (match){
            case MOVIE_WITH_ID:

                String id = uri.getPathSegments().get(1);
                res = database.delete(MovieData.MovieTable.TABLE_NAME,"movie_id=?",new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if(res !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
