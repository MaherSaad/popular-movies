package com.example.mahertag.movieapp.retrofit.Results;

import com.example.mahertag.movieapp.retrofit.Models.VideoModel;

import java.util.List;

/**
 * Created by Mahertag on 3/2/2017.
 */

public class VideosResult {

    private List<VideoModel> results;

    public List<VideoModel> getResults() {
        return results;
    }

    public void setResults(List<VideoModel> results) {
        this.results = results;
    }
}
