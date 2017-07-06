package ru.artempugachev.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;

/**
 * Adapter for movies grid
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviePosterViewHolder> {

    private List<Movie> movies;
    private Context context;
    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    private final MoviesGridClickListener moviesClickListener;

    public MoviesGridAdapter(Context context, MoviesGridClickListener moviesClickListener) {
        this.context = context;
        this.moviesClickListener = moviesClickListener;
    }

    public void setData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public Movie getMovie(int position) {
        if (movies != null && !movies.isEmpty()) {
            return movies.get(position);
        } else {
            return null;
        }
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
            String posterPath = movies.get(position).getPosterPath();
            String imageUrl = BASE_IMAGE_URL + posterPath;
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
            moviesClickListener.onMovieClick(getAdapterPosition());
        }
    }

    public interface MoviesGridClickListener {
        void onMovieClick(int position);
    }

}


