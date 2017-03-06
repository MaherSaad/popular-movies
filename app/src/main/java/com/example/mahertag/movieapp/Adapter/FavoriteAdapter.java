package com.example.mahertag.movieapp.Adapter;

/**
 * Created by Mahertag on 3/3/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahertag.movieapp.Data.MovieData;
import com.example.mahertag.movieapp.R;
import com.example.mahertag.movieapp.databinding.MovieItemBinding;
import com.example.mahertag.movieapp.retrofit.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahertag on 2/18/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder> {
    List<MovieModel> data;
    Context context;
    private Cursor mCursor;
    private MovieItemBinding itemBinding;
    private RecyclerItemClickListenerFav recyclerItemClickListener;

    public FavoriteAdapter(){

    }

    public FavoriteAdapter(Cursor result,Context c,RecyclerItemClickListenerFav itemClickListener) {
        mCursor = result;
        context = c;
        recyclerItemClickListener = itemClickListener;
    }
    public interface RecyclerItemClickListenerFav {
        void onRecyclerItemClickFav(int clickedItemIndex);
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item,parent,false);

        return new MovieViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


            int posterIndex = mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_POSTER);
            int rateIndex = mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_RATE);
            int titleIndex = mCursor.getColumnIndex(MovieData.MovieTable.COLUMN_MOVIE_NAME);

            mCursor.moveToPosition(position);

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185"+mCursor.getString(posterIndex))
                    .placeholder(R.drawable.movie)
                    .error(R.drawable.movie)
                    .into(holder.binding.poster);
            holder.binding.rate.setText(mCursor.getString(rateIndex));
            holder.binding.movieTitle.setText(mCursor.getString(titleIndex));


    }

    @Override
    public int getItemCount() {

            return mCursor.getCount();

    }

    public void FavDelete(){
        this.notifyDataSetChanged();
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
            recyclerItemClickListener.onRecyclerItemClickFav(clickedPosition);
        }
    }
}
