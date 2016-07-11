package saeed.trivago.casestudy.model;

import java.util.ArrayList;

/**
 * A representation of a movie.
 * Created by saeed on 11/07/2016.
 */
public class Movie {

    //Title of the movie
    private String mMovieTitle;

    //Year of released of movie
    private String mMovieYear;

    //Logo URL of movie
    private String mLogoUrl;

    //IDs of movie
    private MovieIds mMovieIds;

    /**
     * Constructs a new movie with a title,year of release, logo and {@code ArrayList} of MovieIds
     *
     * @param mMovieTitle Title of the movie
     * @param mMovieYear  Year of released of the movie
     * @param mLogoUrl    Logo of the movie
     * @param mMovieIds   Ids of the movie
     */
    public Movie(String mMovieTitle, String mMovieYear, String mLogoUrl, MovieIds mMovieIds) {
        this.mMovieTitle = mMovieTitle;
        this.mMovieYear = mMovieYear;
        this.mLogoUrl = mLogoUrl;
        this.mMovieIds = mMovieIds;
    }

    /**
     * Get the movie title
     *
     * @return The movie title
     */
    public String getmMovieTitle() {
        return mMovieTitle;
    }

    /**
     * Get the release year of movie
     *
     * @return the release year of movie
     */
    public String getmMovieYear() {
        return mMovieYear;
    }

    /**
     * Get the logo of movie
     *
     * @return
     */
    public String getmLogoUrl() {
        return mLogoUrl;
    }

    /**
     * Get the ids of movie
     *
     * @return The Ids of movie
     */
    public MovieIds getmMovieIds() {
        return mMovieIds;
    }
}
