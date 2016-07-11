package saeed.trivago.casestudy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.controller.MovieController;
import saeed.trivago.casestudy.listener.MyScrollListener;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.modeladapter.MovieAdapter;

/**
 * Created by saeed on 11/07/2016.
 */
public class MovieActivity extends AppCompatActivity {

    private MovieController mMovieController;
    private ListView mMovieList;
    private ArrayList<Movie> mMovies;
    private MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMovieController = (MovieController) fragmentManager.findFragmentByTag(MovieController.class.getSimpleName());
        if (mMovieController == null) {
            fragmentManager.beginTransaction().add(mMovieController = new MovieController(), MovieController.class.getSimpleName()).commit();
        }

        mMovieList = (ListView) findViewById(R.id.movies);
        /*mMovieList.setOnScrollListener(new MyScrollListener() {
            @Override
            public void loadMoreMovies(int page) {
                mMovieController.getPopularMovies(String.valueOf(page));
            }
        });*/
        mMovieController.getPopularMovies("01");
    }

    public void setUpMovies(ArrayList<Movie> movies) {
        if (mMovies == null) {
            mMovies = new ArrayList<>(movies);
            mMovieAdapter = new MovieAdapter(mMovies);
            mMovieList.setAdapter(mMovieAdapter);
        } else {
            mMovies.addAll(movies);
            mMovieAdapter.notifyDataSetChanged();
        }
    }
}
