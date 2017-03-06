package com.example.mahertag.movieapp.retrofit;

import com.example.mahertag.movieapp.retrofit.Results.MoviesResult;
import com.example.mahertag.movieapp.retrofit.Results.ReviewsResults;
import com.example.mahertag.movieapp.retrofit.Results.VideosResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mahertag on 2/18/2017.
 */

public interface MovieAPI {


    @GET("movie/{type}")
    Call<MoviesResult> getMovies(@Path("type") String type, @Query("api_key") String API_KEY);

    @GET("movie/{id}/videos")
    Call<VideosResult> getVideos(@Path("id") String id,@Query("api_key") String API_KEY);

    @GET("movie/{id}/reviews")
    Call<ReviewsResults> getReviews(@Path("id") String id, @Query("api_key") String API_KEY);

}
