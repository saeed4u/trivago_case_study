package saeed.trivago.casestudy.model;

/**
 * This is a representation of a {@code Movie} ID
 * Created by saeed on 11/07/2016.
 */
public class MovieIds {

    //Trakt ID;
    private String mTraktId;

    //IMDB ID
    private String mIMDBId;

    //Slug ID
    private String mSludId;

    //Tmdb ID
    private String mTmdbId;

    /**
     * Constructs a new movie ID;
     *
     * @param mIMDBId  IMDB Id of movie
     * @param mSludId  Slug Id of movie
     * @param mTmdbId  Tmdb Id of movie
     * @param mTraktId Trakt Id of movie
     */
    public MovieIds(String mIMDBId, String mSludId, String mTmdbId, String mTraktId) {
        this.mIMDBId = mIMDBId;
        this.mSludId = mSludId;
        this.mTmdbId = mTmdbId;
        this.mTraktId = mTraktId;
    }

    public String getmIMDBId() {
        return mIMDBId;
    }

    public String getmSludId() {
        return mSludId;
    }

    public String getmTmdbId() {
        return mTmdbId;
    }

    public String getmTraktId() {
        return mTraktId;
    }
}
