package ru.artempugachev.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.model.Movie;

/**
 * Adapter for movies grid
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviePosterViewHolder> {

    private List<Movie> movies;
    private Context context;
    private final MoviesGridClickListener moviesClickListener;

    public MoviesGridAdapter(Context context, MoviesGridClickListener moviesClickListener) {
        this.context = context;
        this.moviesClickListener = moviesClickListener;
    }

    public void setData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addData(List<Movie> newMovies) {
        if (!newMovies.isEmpty()) {
            int currentSize = movies.size();

            LinkedHashSet<Movie> moviesSet = new LinkedHashSet<Movie>(movies);

            moviesSet.addAll(newMovies);
            int addedSize = moviesSet.size();
            movies = new ArrayList<Movie>(moviesSet);

            notifyItemRangeInserted(currentSize, addedSize);
        }
    }

    public Movie getMovie(int position) {
        if (movies != null && !movies.isEmpty()) {
            return movies.get(position);
        } else {
            return null;
        }
    }


    public ArrayList<Movie> getMovies() {
        return new ArrayList<Movie>(movies);
    }


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
        if (this.movies != null && !this.movies.isEmpty()) {
            String imageUrl = movies.get(position).getFullPosterPath();
            Picasso.with(context).load(imageUrl).into(holder.movieImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }

    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView movieImageView;
        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.posterImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            moviesClickListener.onMovieClick(getAdapterPosition(), v);
        }
    }

    public interface MoviesGridClickListener {
        void onMovieClick(int position, View v);
    }

}


