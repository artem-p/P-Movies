package ru.artempugachev.popularmovies.movielist

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
import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * Adapter for movies grid
 */

class MovieListAdapter(private val context: Context, private val moviesClickListener: MoviesGridClickListener,
                       private val picasso: Picasso) : RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>() {

    private var movies: MutableList<Movie> = ArrayList()

    fun setData(movies: List<Movie>) {
        this.movies = movies.toMutableList()
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

            picasso.load(imageUrl).into(holder.movieImageView)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    fun addMovie(movie: Movie) {
        movies.add(movie)
        notifyItemInserted(movies.size - 1)
    }


    fun empty() {
        movies.clear()
        notifyDataSetChanged()
    }


    inner class MoviePosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val movieImageView: ImageView

        init {
            movieImageView = itemView.findViewById<View>(R.id.posterImage) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            moviesClickListener.onMovieClick(movies[adapterPosition], v)
        }
    }

    interface MoviesGridClickListener {
        fun onMovieClick(movie: Movie, adapterView: View)
    }

}


