package ru.artempugachev.popularmovies.moviedetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.model.Video;

/**
 * Adapter for trailers grid
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private List<Video> mTrailers;
    private Context mContext;
    private TrailerClickListener mTrailerClickListener;

    public TrailersAdapter(Context context, TrailerClickListener trailerClickListener) {
        this.mContext = context;
        this.mTrailerClickListener = trailerClickListener;
    }

    public void setData(List<Video> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public void addData(List<Video> newTrailers) {
        if (!newTrailers.isEmpty()) {
            int currentSize = mTrailers.size();
            int newTrailersSize = newTrailers.size();

            mTrailers.addAll(newTrailers);
            notifyItemRangeInserted(currentSize, newTrailersSize);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int trailerItemLayoutId = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(trailerItemLayoutId, parent, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        if (mTrailers != null && !mTrailers.isEmpty()) {
            Video trailer = mTrailers.get(position);
            String trailerUrl = trailer.getThumbnailUrl();

            Picasso.with(mContext).load(trailerUrl).into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mTrailers != null ? mTrailers.size() : 0;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView thumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Video trailer = mTrailers.get(getAdapterPosition());
            String urlStr = trailer.getVideoUrl();
            mTrailerClickListener.onTrailerClick(urlStr);
        }
    }

    public interface TrailerClickListener {
        void onTrailerClick(String urlStr);
    }
}
