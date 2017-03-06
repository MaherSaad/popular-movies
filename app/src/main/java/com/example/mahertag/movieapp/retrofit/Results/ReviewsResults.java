package com.example.mahertag.movieapp.retrofit.Results;

import com.example.mahertag.movieapp.retrofit.Models.ReviewModel;

import java.util.List;

/**
 * Created by Mahertag on 3/2/2017.
 */

public class ReviewsResults {

    private List<ReviewModel> results;

    public List<ReviewModel> getResults() {
        return results;
    }

    public void setResults(List<ReviewModel> results) {
        this.results = results;
    }
}
