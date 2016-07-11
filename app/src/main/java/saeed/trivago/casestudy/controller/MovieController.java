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
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.MovieIds;
import saeed.trivago.casestudy.ui.MovieActivity;

import static saeed.trivago.casestudy.util.AppConstants.CONTENT_TYPE;
import static saeed.trivago.casestudy.util.AppConstants.IS_DEBUG;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_KEY;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_KEY_VALUE;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_VERSION;
import static saeed.trivago.casestudy.util.AppConstants.TRAKT_API_VERSION_VALUE;
import static saeed.trivago.casestudy.util.AppConstants.URL_MOVIE_POPULAR;

/**
 * Created by saeed on 11/07/2016.
 */
public class MovieController extends Fragment {

    private MovieActivity mFirstActivity;
    private GetPopularMovies mGetPopularMovies;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirstActivity = (MovieActivity) context;
    }

    public void getPopularMovies(String page) {
        mGetPopularMovies = new GetPopularMovies();
        mGetPopularMovies.execute(page);
    }

    public void cancelRequestPopular() {
        if (mGetPopularMovies != null && (mGetPopularMovies.getStatus() == AsyncTask.Status.RUNNING || mGetPopularMovies.getStatus() == AsyncTask.Status.PENDING)) {
            mGetPopularMovies.cancel(true);
        }
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    private class GetPopularMovies extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: 11/07/2016 show progress view
        }

        private Call mCall;

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            String url = buildPopularUrl(params[0]);
            ArrayList<Movie> movies = new ArrayList<>();
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
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mCall != null) {
                mCall.cancel();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mFirstActivity.setUpMovies(movies);
        }
    }


    private String buildPopularUrl(String page) {
        StringBuilder urlBuilder = new StringBuilder(URL_MOVIE_POPULAR);
        urlBuilder.append("&page=").append(page);
        String url = urlBuilder.toString();
        Log.v("URL", url);
        return url;
    }
}
