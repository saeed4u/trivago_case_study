package saeed.trivago.casestudy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import saeed.trivago.casestudy.controller.MovieController;

/**
 * Created by saeed on 11/07/2016.
 */
public class MovieActivity extends AppCompatActivity {

    private MovieController mMovieController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mMovieController = (MovieController) fragmentManager.findFragmentByTag(MovieController.class.getSimpleName());
        if (mMovieController == null) {
            fragmentManager.beginTransaction().add(mMovieController = new MovieController(), MovieController.class.getSimpleName()).commit();
        }


    }
}
