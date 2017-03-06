package com.example.mahertag.movieapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahertag.movieapp.R;
import com.example.mahertag.movieapp.databinding.VideoItemBinding;
import com.example.mahertag.movieapp.retrofit.Models.VideoModel;

import java.util.List;

/**
 * Created by Mahertag on 3/2/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<VideoModel> data;
    Context context;
    private VideoItemBinding itemBinding;
    private RecyclerItemClickListener recyclerItemClickListener;

    public VideoAdapter(List<VideoModel> result,Context c,RecyclerItemClickListener itemClickListener) {
        data = result;
        context = c;
        recyclerItemClickListener = itemClickListener;
    }


    public interface RecyclerItemClickListener {
        void onRecyclerItemClick(int clickedItemIndex);
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.video_item,parent,false);

        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {


        holder.binding.videoName.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VideoItemBinding binding;
        public VideoViewHolder(VideoItemBinding itemView) {
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
