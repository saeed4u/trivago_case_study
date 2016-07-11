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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.MovieId;
import saeed.trivago.casestudy.ui.FirstActivity;

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

    private FirstActivity mFirstActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirstActivity = (FirstActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    private class GetPopularMovie extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: 11/07/2016 show progress view
        }

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
                Response okHttpResponse = okHttpClient.newCall(request).execute();
                String response = okHttpResponse.body().string();
                if (IS_DEBUG) {
                    Log.v("Response", response);
                }
                JSONArray responseJson = new JSONArray(response);

                for (int i = 0; i < responseJson.length(); i++) {
                    JSONObject movieJSON = responseJson.getJSONObject(i);

                    String movieTitle = movieJSON.has("title") ? movieJSON.getString("title") : "N/A";
                    String movieYear = movieJSON.has("year") ? movieJSON.getString("year") : "N/A";
                    ArrayList<MovieId> movieIds = new ArrayList<>();
                    if (movieJSON.has("ids")) {
                        JSONObject ids = movieJSON.getJSONObject("ids");
                        String trakt = ids.has("trakt") ? String.valueOf(ids.getInt("trakt")) : "N/A";
                        String slug = ids.has("slug") ? ids.getString("slug") : "N/A";
                        String imdb = ids.has("imdb") ? ids.getString("imdb") : "N/A";
                        String tmdb = ids.has("tmdb") ? ids.getString("tmdb") : "N/A";

                        MovieId traktId = new MovieId("Trakt", trakt);
                        MovieId slugId = new MovieId("Slug", slug);
                        MovieId imdbId = new MovieId("IMDB", imdb);
                        MovieId tmdbId = new MovieId("TMDb", tmdb);

                        movieIds.add(traktId);
                        movieIds.add(slugId);
                        movieIds.add(imdbId);
                        movieIds.add(tmdbId);
                    }
                    String logoUrl = "http://";
                    if (movieJSON.has("images")) {
                        JSONObject imageObject = movieJSON.getJSONObject("images");
                        if (imageObject.has("logo")) {
                            JSONObject logoObject = imageObject.getJSONObject("logo");
                            logoUrl = logoObject.has("full") ? logoObject.getString("full") : "http://";
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
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            // TODO: 11/07/2016 call activity's listview adapter
        }
    }


    private String buildPopularUrl(String page) {
        StringBuilder urlBuilder = new StringBuilder(URL_MOVIE_POPULAR);
        urlBuilder.append("&page=").append(page);
        return urlBuilder.toString();
    }
}
