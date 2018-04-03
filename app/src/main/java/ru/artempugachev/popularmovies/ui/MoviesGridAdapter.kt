package ru.artempugachev.popularmovies.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.squareup.picasso.Picasso

import java.util.ArrayList
import java.util.LinkedHashSet

import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.model.Movie

/**
 * Adapter for movies grid
 */

class MoviesGridAdapter(private val context: Context, private val moviesClickListener: MoviesGridClickListener) : RecyclerView.Adapter<MoviesGridAdapter.MoviePosterViewHolder>() {

    private lateinit var movies: List<Movie>

    fun setData(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    fun addData(newMovies: List<Movie>) {
        if (!newMovies.isEmpty()) {
            val currentSize = movies.size

            val moviesSet = LinkedHashSet(movies)

            moviesSet.addAll(newMovies)
            val addedSize = moviesSet.size
            movies = ArrayList(moviesSet)

            notifyItemRangeInserted(currentSize, addedSize)
        }
    }

    fun getMovie(position: Int): Movie? {
        return if (!movies.isEmpty()) {
            movies[position]
        } else {
            null
        }
    }


    fun getMovies(): ArrayList<Movie> {
        return ArrayList(movies)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val context = parent.context
        val posterItemLayoutId = R.layout.movie_grid_item_layout
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(posterItemLayoutId, parent, shouldAttachToParentImmediately)
        return MoviePosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        if (!this.movies.isEmpty()) {
            val imageUrl = movies[position].fullPosterPath
            Picasso.with(context).load(imageUrl).into(holder.movieImageView)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    inner class MoviePosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val movieImageView: ImageView

        init {
            movieImageView = itemView.findViewById<View>(R.id.posterImage) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            moviesClickListener.onMovieClick(adapterPosition, v)
        }
    }

    interface MoviesGridClickListener {
        fun onMovieClick(position: Int, v: View)
    }

}


