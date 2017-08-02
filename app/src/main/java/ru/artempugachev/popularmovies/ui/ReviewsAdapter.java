package ru.artempugachev.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.Review;

/**
 * Adapter for reviews recycler
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> reviews;

    public ReviewsAdapter() {
    }

    public void setData(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public void addData(List<Review> newReviews) {
        if (!newReviews.isEmpty()) {
            int currentSize = reviews.size();
            int newReviewsSize = newReviews.size();

            reviews.addAll(newReviews);
            notifyItemRangeInserted(currentSize, newReviewsSize);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int reviewItemLayoutId = R.layout.review_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(reviewItemLayoutId, parent, shouldAttachToParentImmediately);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (reviews != null && !reviews.isEmpty()) {
            Review review = reviews.get(position);
            String author = review.getAuthor();
            String content = review.getContent();

            holder.author.setText(author);
            holder.content.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
