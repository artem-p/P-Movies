package ru.artempugachev.popularmovies.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.artempugachev.popularmovies.data.Video;

/**
 * Adapter for trailers grid
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private List<Video> mTrailers;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mTrailers != null ? mTrailers.size() : 0;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        public TrailerViewHolder(View itemView) {
            super(itemView);
        }
    }

}
