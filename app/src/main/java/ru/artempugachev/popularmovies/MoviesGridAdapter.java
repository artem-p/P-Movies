package ru.artempugachev.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter for movies grid
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviePosterViewHolder> {

    // todo this is for debug, delete after implementing grid with real data
    private final static int POSTER_GRID_STUB_NUMBER = 30;

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int posterItemLayoutId = R.layout.movie_grid_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(posterItemLayoutId, parent, shouldAttachToParentImmediately);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // todo implement with real data
        return POSTER_GRID_STUB_NUMBER;
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder {
        public MoviePosterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
