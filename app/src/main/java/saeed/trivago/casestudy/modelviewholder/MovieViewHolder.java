package saeed.trivago.casestudy.modelviewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;

/**
 * Created by saeed on 12/07/2016.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView mMovieImage;

    public MovieViewHolder(View itemView) {
        super(itemView);
        mMovieImage = (ImageView) itemView.findViewById(R.id.movie_image);
    }

    public void bindData(Movie movie) {
        Picasso.with(mMovieImage.getContext()).load(movie.getmLogoUrl()).error(R.drawable.ic_movie_white_48dp).placeholder(R.drawable.ic_movie_white_48dp).into(mMovieImage);
    }
}
