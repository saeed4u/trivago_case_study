package saeed.trivago.casestudy.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.MovieIds;
import saeed.trivago.casestudy.model.SearchedMovie;
import saeed.trivago.casestudy.ui.MovieActivity;

import static saeed.trivago.casestudy.util.AppConstants.CONTENT_TYPE;
import static saeed.trivago.casestudy.util.AppConstants.IS_DEBUG;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_KEY;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_KEY_VALUE;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_VERSION;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_VERSION_VALUE;
import static saeed.trivago.casestudy.util.AppConstants.URL_MOVIE_POPULAR;
import static saeed.trivago.casestudy.util.AppConstants.URL_MOVIE_SEARCH;

/**
 * Created by saeed on 11/07/2016.
 */
public class MovieController extends Fragment {

    private MovieActivity mFirstActivity;
    private GetPopularMovies mGetPopularMovies;
    private GetMorePopularMovies mGetMorePopularMovies;
    private SearchMovies mSearchMovie;

    private Call mCall;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirstActivity = (MovieActivity) context;
    }

    public void getPopularMovies(String page) {
        mGetPopularMovies = new GetPopularMovies();
        mGetPopularMovies.execute(page);
    }

    public void getMoreMovies(String page) {
        mGetMorePopularMovies = new GetMorePopularMovies();
        mGetMorePopularMovies.execute(page);
    }

    public void searchMovie(String query, String page) {
        cancelSearch();
        mSearchMovie = new SearchMovies();
        mSearchMovie.execute(query, page);
    }

    public void cancelRequestPopular() {
        if (mGetPopularMovies != null && (mGetPopularMovies.getStatus() == AsyncTask.Status.RUNNING || mGetPopularMovies.getStatus() == AsyncTask.Status.PENDING)) {
            mGetPopularMovies.cancel(true);
        } else if (mGetMorePopularMovies != null && (mGetMorePopularMovies.getStatus() == AsyncTask.Status.RUNNING || mGetMorePopularMovies.getStatus() == AsyncTask.Status.PENDING)) {
            mGetMorePopularMovies.cancel(true);
        }
    }

    private void cancelSearch() {
        if (mSearchMovie != null && (mSearchMovie.getStatus() == AsyncTask.Status.RUNNING || mSearchMovie.getStatus() == AsyncTask.Status.PENDING)) {
            mSearchMovie.cancel(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private class GetPopularMovies extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            String url = buildPopularUrl(params[0]);
            return getMovies(url, false);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mCall != null) {
                mCall.cancel();
            }
        }

        @Override
        protected void onPostExecute(Object movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                mFirstActivity.setUpPopularMovies((ArrayList<Movie>) movies);
            }
        }
    }

    @Nullable
    private Object getMovies(String url, boolean isSearch) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("Content-Type", CONTENT_TYPE)
                    .addHeader(TRAKT_API_KEY, TRAKT_API_KEY_VALUE)
                    .addHeader(TRAKT_API_VERSION, TRAKT_API_VERSION_VALUE)
                    .url(url)
                    .get()
                    .build();
            mCall = okHttpClient.newCall(request);
            Response okHttpResponse = mCall.execute();
            String response = okHttpResponse.body().string();
            // String headers = okHttpResponse.headers().
            if (IS_DEBUG) {
                Log.v("Response", "Response  = " + response);
            }
            JSONArray responseJson = new JSONArray(response);
            if (!isSearch) {
                return parseJSONPopular(responseJson);
            } else {
                return parseJSONSearch(responseJson);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            mFirstActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFirstActivity.setEmptyListText(getString(R.string.oops_error_occured));
                }
            });
        }
        return null;
    }

    private Object parseJSONPopular(JSONArray responseJson) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < responseJson.length(); i++) {
            JSONObject movieJSON = responseJson.getJSONObject(i);

            String movieTitle = movieJSON.has("title") ? movieJSON.getString("title") : "N/A";
            String movieYear = movieJSON.has("year") ? movieJSON.getString("year") : "N/A";
            MovieIds movieIds = null;
            if (movieJSON.has("ids")) {
                JSONObject ids = movieJSON.getJSONObject("ids");
                String trakt = ids.has("trakt") ? String.valueOf(ids.getInt("trakt")) : "N/A";
                String slug = ids.has("slug") ? ids.getString("slug") : "N/A";
                String imdb = ids.has("imdb") ? ids.getString("imdb") : "N/A";
                String tmdb = ids.has("tmdb") ? ids.getString("tmdb") : "N/A";

                movieIds = new MovieIds(imdb, slug, tmdb, trakt);
            }
            String logoUrl = "http://";
            if (movieJSON.has("images")) {
                JSONObject imageObject = movieJSON.getJSONObject("images");
                if (imageObject.has("poster")) {
                    JSONObject logoObject = imageObject.getJSONObject("poster");
                    logoUrl = logoObject.has("thumb") ? logoObject.getString("thumb") : "http://";
                }
            }

            Movie movie = new Movie(movieTitle, movieYear, logoUrl, movieIds);
            movies.add(movie);
        }
        return movies;
    }

    private Object parseJSONSearch(JSONArray responseJson) throws JSONException {
        ArrayList<SearchedMovie> movies = new ArrayList<>();
        for (int i = 0; i < responseJson.length(); i++) {
            JSONObject searchJSON = responseJson.getJSONObject(i);

            String movieScore = searchJSON.has("score") ? searchJSON.getString("score") : "";

            JSONObject movieJSON = searchJSON.getJSONObject("movie");

            String movieTitle = movieJSON.has("title") ? movieJSON.getString("title") : "N/A";
            String movieYear = movieJSON.has("year") ? movieJSON.getString("year") : "N/A";
            MovieIds movieIds = null;
            if (movieJSON.has("ids")) {
                JSONObject ids = movieJSON.getJSONObject("ids");
                String trakt = ids.has("trakt") ? String.valueOf(ids.getInt("trakt")) : "N/A";
                String slug = ids.has("slug") ? ids.getString("slug") : "N/A";
                String imdb = ids.has("imdb") ? ids.getString("imdb") : "N/A";
                String tmdb = ids.has("tmdb") ? ids.getString("tmdb") : "N/A";

                movieIds = new MovieIds(imdb, slug, tmdb, trakt);
            }
            String logoUrl = "http://";
            if (movieJSON.has("images")) {
                JSONObject imageObject = movieJSON.getJSONObject("images");
                if (imageObject.has("poster")) {
                    JSONObject logoObject = imageObject.getJSONObject("poster");
                    logoUrl = logoObject.has("thumb") ? logoObject.getString("thumb") : "http://";
                }
            }

            Movie movie = new Movie(movieTitle, movieYear, logoUrl, movieIds);
            SearchedMovie searchedMovie = new SearchedMovie(movieScore, movie);
            movies.add(searchedMovie);
        }
        return movies;
    }

    private class GetMorePopularMovies extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            String url = buildPopularUrl(params[0]);
            return getMovies(url, false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mCall != null) {
                mCall.cancel();
            }
        }

        @Override
        protected void onPostExecute(Object movies) {
            super.onPostExecute(movies);
            mFirstActivity.setUpPopularMovies((ArrayList<Movie>) movies);
        }
    }

    private class SearchMovies extends AsyncTask<String, Void, Object> {


        @Override
        protected Object doInBackground(String... strings) {
            String url = buildSearchUrl(strings);
            return getMovies(url, true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mCall != null) {
                mCall.cancel();
            }
        }

        @Override
        protected void onPostExecute(Object movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                mFirstActivity.setUpSearchedMovies((ArrayList<SearchedMovie>) movies);
            }
        }
    }

    private String buildPopularUrl(String page) {
        StringBuilder urlBuilder = new StringBuilder(URL_MOVIE_POPULAR);
        urlBuilder.append("&page=").append(page);
        String url = urlBuilder.toString();
        Log.v("URL", url);
        return url;
    }

    private String buildSearchUrl(String... values) {
        StringBuilder urlBuilder = new StringBuilder(URL_MOVIE_SEARCH);
        urlBuilder.append("&page=").append(values[0]);
        urlBuilder.append("&query=").append(values[1]);
        String url = urlBuilder.toString();
        Log.v("URL", url);
        return url;
    }
}
