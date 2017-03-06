package com.example.mahertag.movieapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahertag on 3/3/2017.
 */

public class MovieData {


    public static final String CONTENT_AUTHORITY = "com.example.mahertag.movieapp";

    public static final Uri BASE_URL = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String MOVIE_PATH = "movie";



    public static class MovieTable implements BaseColumns {

        public static final Uri CONTENT_PROVIDER_URI = BASE_URL.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_NAME = "name";

        public static final String COLUMN_MOVIE_RELASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_RATE = "rate";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_POSTER = "poster";

        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";


    }
}
