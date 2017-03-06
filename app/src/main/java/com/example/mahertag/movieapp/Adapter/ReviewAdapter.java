package com.example.mahertag.movieapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mahertag.movieapp.R;
import com.example.mahertag.movieapp.databinding.ReviewItemBinding;
import com.example.mahertag.movieapp.retrofit.Models.ReviewModel;

import java.util.List;

/**
 * Created by Mahertag on 3/2/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    List<ReviewModel> data;
    Context context;
    private ReviewItemBinding itemBinding;

    public ReviewAdapter(List<ReviewModel> result,Context c) {
        data = result;
        context = c;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.review_item,parent,false);

        return new ReviewViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {


        holder.binding.author.setText(data.get(position).getAuthor());
        holder.binding.content.setText(data.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ReviewItemBinding binding;
        public ReviewViewHolder(ReviewItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}
