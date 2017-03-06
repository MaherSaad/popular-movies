package com.example.mahertag.movieapp.retrofit.Results;

import com.example.mahertag.movieapp.retrofit.MovieModel;

import java.util.List;

//import com.example.mahertag.movieapp.retrofit.Models.
/**
 * Created by Mahertag on 2/18/2017.
 */

public class MoviesResult {
    private List<MovieModel> results;

    public List<MovieModel> getResults() {
        return results;
    }
    public void setResults(List<MovieModel> results) {
        this.results = results;
    }
}
