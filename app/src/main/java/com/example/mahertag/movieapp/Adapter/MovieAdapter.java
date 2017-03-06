package com.example.mahertag.movieapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahertag.movieapp.R;
import com.example.mahertag.movieapp.databinding.MovieItemBinding;
import com.example.mahertag.movieapp.retrofit.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahertag on 2/18/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<MovieModel> data;
    Context context;
    private MovieItemBinding itemBinding;
    private RecyclerItemClickListener recyclerItemClickListener;

    public MovieAdapter(List<MovieModel> result,Context c,RecyclerItemClickListener itemClickListener) {
        data = result;
        context = c;
        recyclerItemClickListener = itemClickListener;
    }


    public interface RecyclerItemClickListener {
        void onRecyclerItemClick(int clickedItemIndex);
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item,parent,false);

        return new MovieViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185"+data.get(position).getPoster_path())
                    .placeholder(R.drawable.movie)
                    .error(R.drawable.movie)
                    .into(holder.binding.poster);
            holder.binding.rate.setText(data.get(position).getVote_average());
            holder.binding.movieTitle.setText(data.get(position).getOriginal_title());


    }

    @Override
    public int getItemCount() {


        return data.size();
    }



    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MovieItemBinding binding;
        public MovieViewHolder(MovieItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            itemView.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            recyclerItemClickListener.onRecyclerItemClick(clickedPosition);
        }
    }
}
