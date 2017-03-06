package com.example.mahertag.movieapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mahertag.movieapp.R;

/**
 * Created by Mahertag on 3/1/2017.
 */

public class PrefUtility {

    public static void setViewType(Context context,String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_movies_key),value);
        editor.apply();
    }

    public static String getViewType(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_movies_key),context.getString(R.string.pref_default_value));
    }
}
