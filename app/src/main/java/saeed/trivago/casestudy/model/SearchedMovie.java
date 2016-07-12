package saeed.trivago.casestudy.model;

/**
 * Created by saeed on 12/07/2016.
 */
public class SearchedMovie {

    private String mMovieScore;
    private Movie mMovie;

    public SearchedMovie(String mMovieScore, Movie mMovie) {
        this.mMovieScore = mMovieScore;
        this.mMovie = mMovie;
    }

    public String getmMovieScore() {
        return mMovieScore;
    }

    public Movie getmMovie() {
        return mMovie;
    }
}
