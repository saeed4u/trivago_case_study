package saeed.trivago.casestudy.modelviewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.MovieIds;
import saeed.trivago.casestudy.model.SearchedMovie;

/**
 * Created by saeed on 12/07/2016.
 */
public class SearchMovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView mMovieImage;
    private TextView mMovieTitle;
    private TextView mMovieYear;
    private TextView mMovieTraktID;
    private TextView mMovieSlugID;
    private TextView mMovieIMDbID;
    private TextView mMovieTMDbID;
    private TextView mMovieScore;


    public SearchMovieViewHolder(View itemView) {
        super(itemView);
        mMovieImage = (ImageView) itemView.findViewById(R.id.movie_image);
        mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
        mMovieYear = (TextView) itemView.findViewById(R.id.movie_year);
        mMovieTraktID = (TextView) itemView.findViewById(R.id.trakt);
        mMovieSlugID = (TextView) itemView.findViewById(R.id.slug);
        mMovieIMDbID = (TextView) itemView.findViewById(R.id.imdb);
        mMovieTMDbID = (TextView) itemView.findViewById(R.id.tmdb);
        mMovieScore = (TextView) itemView.findViewById(R.id.movie_score);

    }

    public void bindData(SearchedMovie searchedMovie) {
        Movie movie = searchedMovie.getmMovie();
        Picasso.with(mMovieImage.getContext()).load(movie.getmLogoUrl()).error(R.drawable.ic_movie_white_48dp).placeholder(R.drawable.ic_movie_white_48dp).into(mMovieImage);
        mMovieTitle.setText(movie.getmMovieTitle());
        mMovieYear.setText(movie.getmMovieYear());
        MovieIds movieIds = movie.getmMovieIds();
        mMovieTraktID.setText(mMovieTraktID.getContext().getString(R.string.trakt_id, movieIds.getmTraktId()));
        mMovieSlugID.setText(mMovieSlugID.getContext().getString(R.string.slug_id, movieIds.getmSludId()));
        mMovieIMDbID.setText(mMovieIMDbID.getContext().getString(R.string.imdb_id, movieIds.getmIMDBId()));
        mMovieTMDbID.setText(mMovieTMDbID.getContext().getString(R.string.tmdb_id, movieIds.getmTmdbId()));
        mMovieScore.setText(mMovieScore.getContext().getString(R.string.movie_score, searchedMovie.getmMovieScore()));
    }

}
