package saeed.trivago.casestudy.model;

/**
 * This is a key-value representation of a {@code Movie} ID
 * Created by saeed on 11/07/2016.
 */
public class MovieId {

    //Key of MovieId;
    private String mKey;

    //Value of MovieId
    private String mValue;

    /**
     * Constructs a new MovieId with the specified key and value.
     * @param mKey   {@code #mKey} for this MovieId
     * @param mValue {@code mValue} for the MovieId
     */
    public MovieId(String mKey, String mValue) {
        this.mKey = mKey;
        this.mValue = mValue;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmValue() {
        return mValue;
    }
}
