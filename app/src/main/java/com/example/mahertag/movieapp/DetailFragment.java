package com.example.mahertag.movieapp;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mahertag.movieapp.Adapter.ReviewAdapter;
import com.example.mahertag.movieapp.Adapter.VideoAdapter;
import com.example.mahertag.movieapp.Data.MovieData;
import com.example.mahertag.movieapp.databinding.FragmentDetailBinding;
import com.example.mahertag.movieapp.retrofit.Models.ReviewModel;
import com.example.mahertag.movieapp.retrofit.Models.VideoModel;
import com.example.mahertag.movieapp.retrofit.MovieAPI;
import com.example.mahertag.movieapp.retrofit.MovieModel;
import com.example.mahertag.movieapp.retrofit.Results.ReviewsResults;
import com.example.mahertag.movieapp.retrofit.Results.VideosResult;
import com.example.mahertag.movieapp.retrofit.init;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements VideoAdapter.RecyclerItemClickListener {

    FragmentDetailBinding binding;
    private List<VideoModel> listVideos;
    private List<ReviewModel> listReviews;
    private MovieModel movieModel;
    private boolean Fav = false;
    private Menu lMenu;
    private boolean TABLET;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false);
        View v = binding.getRoot();
        Bundle b;
        if(getArguments() == null){
            Intent intent = getActivity().getIntent();
             b= intent.getExtras();
        }else {
            TABLET = true;
            b = getArguments();
        }


        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar !=null && !TABLET){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        movieModel =  Parcels.unwrap(b.getParcelable("Movie"));

        binding.setMovieDetail(movieModel);

        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185"+movieModel.getPoster_path())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(binding.Poster);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w342"+movieModel.getBackdrop_path())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(binding.posterBackground);

        /**************************************************************/

        CheckFav();

        MovieAPI api = init.initClient().create(MovieAPI.class);

        binding.videos.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.videos.setHasFixedSize(true);

        binding.reviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.reviews.setHasFixedSize(true);

        getVideos(movieModel.getId(),api);
        getReviews(movieModel.getId(),api);


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
        lMenu = menu;
        if (Fav){
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getActivity().finish();
        }else if (id == R.id.action_share){

            String url = "http://www.youtube.com/watch?v="+listVideos.get(0).getKey();

            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType("text/plain")
                    .setChooserTitle("Share This Movie")
                    .setText(movieModel.getOriginal_title() + "\n\n" + url)
                    .startChooser();

        }else if(id == R.id.action_favorite){
            if (Fav){

                Uri delete = MovieData.MovieTable.CONTENT_PROVIDER_URI.buildUpon().appendPath(movieModel.getId()).build();
                int res = getActivity().getContentResolver().delete(delete,null,null);

                if(res != 0){
                    item.setIcon(R.drawable.ic_unfavorite);
                    Fav = false;
                    PrintMessage("Movie Removed From Favorites");
                }

            }else {


                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_ID,movieModel.getId());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_NAME,movieModel.getOriginal_title());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_RELASE_DATE,movieModel.getRelease_date());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_RATE,movieModel.getVote_average());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_OVERVIEW,movieModel.getOverview());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_POSTER,movieModel.getPoster_path());
                contentValues.put(MovieData.MovieTable.COLUMN_MOVIE_BACKDROP,movieModel.getBackdrop_path());

                Uri uri = getActivity().getContentResolver().insert(MovieData.MovieTable.CONTENT_PROVIDER_URI,contentValues);

                if (uri !=null){
                    Fav = true;
                    item.setIcon(R.drawable.ic_favorite);
                    PrintMessage("Movie Added To Favourite!");
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }




    /* Get Videos */
    private void getVideos(String id,MovieAPI api){

        Call<VideosResult> videosResultCall = api.getVideos(id,getActivity().getString(R.string.API_KEY));

        videosResultCall.enqueue(new Callback<VideosResult>() {
            @Override
            public void onResponse(Call<VideosResult> call, Response<VideosResult> response) {
                listVideos = response.body().getResults();
                binding.videos.setAdapter(new VideoAdapter(listVideos,getActivity(),DetailFragment.this));
            }

            @Override
            public void onFailure(Call<VideosResult> call, Throwable t) {

            }
        });
    }

    /* Get Reviews */
    private void getReviews(String id,MovieAPI api){

        Call<ReviewsResults> reviewsResultsCall = api.getReviews(id,getActivity().getString(R.string.API_KEY));

        reviewsResultsCall.enqueue(new Callback<ReviewsResults>() {
            @Override
            public void onResponse(Call<ReviewsResults> call, Response<ReviewsResults> response) {
                listReviews = response.body().getResults();
                binding.reviews.setAdapter(new ReviewAdapter(listReviews,getActivity()));
            }

            @Override
            public void onFailure(Call<ReviewsResults> call, Throwable t) {

            }
        });

    }


    @Override
    public void onRecyclerItemClick(int clickedItemIndex) {

        String url = "http://www.youtube.com/watch?v="+listVideos.get(clickedItemIndex).getKey();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }

    private void PrintMessage(String Message){
        Toast.makeText(getActivity(),Message,Toast.LENGTH_SHORT).show();
    }

    private void CheckFav(){
        Uri searchQuery = MovieData.MovieTable.CONTENT_PROVIDER_URI.buildUpon().appendPath(movieModel.getId()).build();
        Cursor cursor = getActivity().getContentResolver().query(searchQuery,null,null,null,null);
        if (cursor.getCount() > 0){
            Fav = true;
        }

    }
}
