package com.example.mahertag.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mahertag.movieapp.Adapter.FavoriteAdapter;
import com.example.mahertag.movieapp.Adapter.MovieAdapter;
import com.example.mahertag.movieapp.Data.MovieData;
import com.example.mahertag.movieapp.Utils.PrefUtility;
import com.example.mahertag.movieapp.databinding.ActivityMainBinding;
import com.example.mahertag.movieapp.retrofit.MovieAPI;
import com.example.mahertag.movieapp.retrofit.MovieModel;
import com.example.mahertag.movieapp.retrofit.Results.MoviesResult;
import com.example.mahertag.movieapp.retrofit.init;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.RecyclerItemClickListener,FavoriteAdapter.RecyclerItemClickListenerFav {

    private String LOG = getClass().getName();
    private ActivityMainBinding binding;
    private List<MovieModel> data;
    private Cursor mCursor;
    private boolean Fav;
    private boolean TABLET;
    private GridLayoutManager gridLayoutManager;
    int movedPosition;
    boolean saveInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

        if (savedInstanceState !=null){
            movedPosition = savedInstanceState.getInt("POS",0);
            saveInstance = true;
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.moviesRecycler.setHasFixedSize(true);


        if(findViewById(R.id.FragmentManager) !=null){
            TABLET = true;
        }

        int grid ;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            grid = 2;
        } else {
            grid = 3;
        }

        gridLayoutManager = new GridLayoutManager(this,grid);
        binding.moviesRecycler.setLayoutManager(gridLayoutManager);
        getHistory();

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });


    }



    /*
    Get Data From Movies DB
     */

    private void getData(String type){
        binding.errorMessage.setVisibility(View.GONE);
        binding.loadingbar.setVisibility(View.VISIBLE);
        MovieAPI movieAPI = init.initClient().create(MovieAPI.class);

        Call<MoviesResult> call = movieAPI.getMovies(type,getString(R.string.API_KEY));

        call.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                binding.errorMessage.setVisibility(View.GONE);
                binding.moviesRecycler.setVisibility(View.VISIBLE);

                List<MovieModel> results = response.body().getResults();
                Log.e(LOG,"Length is " + results.size());
                data = results;
                binding.moviesRecycler.setAdapter(new MovieAdapter(results,MainActivity.this,MainActivity.this));
                binding.moviesRecycler.smoothScrollToPosition(movedPosition);
                binding.loadingbar.setVisibility(View.INVISIBLE);
                binding.swipeLayout.setRefreshing(false);


                if(TABLET && !saveInstance){
                    Bundle b = new Bundle();
                    b.putParcelable("Movie", Parcels.wrap(data.get(0)));
                    DetailFragment fragment = new DetailFragment();
                    fragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentManager,fragment).commit();
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                Log.e(LOG,t.toString());
                binding.errorMessage.setText(t.getMessage());
                binding.errorMessage.setVisibility(View.VISIBLE);
                binding.moviesRecycler.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        String type = null;
        if (id == R.id.popular) {
            type = "popular";
            binding.prefTitle.setText(getString(R.string.pref_pop_movies));
            PrefUtility.setViewType(this,type);
            getData(type);
            Fav = false;

        } else if (id == R.id.topRated) {
            type="top_rated";
            binding.prefTitle.setText(getString(R.string.pref_top_movies));
            PrefUtility.setViewType(this,type);
            getData(type);
            Fav = false;

        }else if (id == R.id.favorites) {
            binding.prefTitle.setText(getString(R.string.pref_fav_movies));
            PrefUtility.setViewType(this,"favorite");
            getFavorites();
            Fav = true;
        }


        return super.onOptionsItemSelected(item);
    }



    private void getHistory(){

        String pref = PrefUtility.getViewType(this);
        if(pref.equals("popular")){
            binding.prefTitle.setText(getString(R.string.pref_pop_movies));
            getData(pref);
        }else if(pref.equals("top_rated")){
            binding.prefTitle.setText(getString(R.string.pref_top_movies));
            getData(pref);
        }else if(pref.equals("favorite")){
            binding.prefTitle.setText(getString(R.string.pref_fav_movies));
            getFavorites();
            Fav = true;
        }
    }

    private void getFavorites(){
         mCursor = getContentResolver().query(MovieData.MovieTable.CONTENT_PROVIDER_URI,null,null,null,null);
        if(mCursor.getCount()>0){
            binding.moviesRecycler.setAdapter(new FavoriteAdapter(mCursor, this, this));
            binding.errorMessage.setVisibility(View.GONE);
            binding.moviesRecycler.smoothScrollToPosition(movedPosition);

            if(TABLET && !saveInstance){
                Bundle b = new Bundle();
                b.putParcelable("Movie", Parcels.wrap(cursorToModel(0)));
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentManager,fragment).commit();
            }
        }else {
            binding.moviesRecycler.removeAllViewsInLayout();
            binding.errorMessage.setText("No Favorites Found!");
            binding.errorMessage.setVisibility(View.VISIBLE);
        }
        binding.swipeLayout.setRefreshing(false);

    }

    @Override
    public void onRecyclerItemClickFav(int clickedItemIndex) {
        Bundle b = new Bundle();

        b.putParcelable("Movie", Parcels.wrap(cursorToModel(clickedItemIndex)));

        if(TABLET){
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentManager,fragment).commit();
        }else {
            Intent intent = new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtras(b);
            startActivity(intent);

        }


    }

    @Override
    public void onRecyclerItemClick(int clickedItemIndex) {
        Bundle b = new Bundle();

        b.putParcelable("Movie", Parcels.wrap(data.get(clickedItemIndex)));

        if(TABLET){
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentManager,fragment).commit();
        }else {
            Intent intent = new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }




    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

            outState.putInt("POS",gridLayoutManager.findFirstVisibleItemPosition());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Fav){
            getFavorites();
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private MovieModel cursorToModel(int clickedItemIndex){
        mCursor.moveToPosition(clickedItemIndex);
        MovieModel movieModel = new MovieModel();
        movieModel.setId(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_ID)));
        movieModel.setBackdrop_path(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_BACKDROP)));
        movieModel.setOriginal_title(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_NAME)));
        movieModel.setOverview(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_OVERVIEW)));
        movieModel.setPoster_path(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_POSTER)));
        movieModel.setRelease_date(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_RELASE_DATE)));
        movieModel.setVote_average(mCursor.getString(mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_RATE)));
        return movieModel;
    }
}
