package saeed.trivago.casestudy.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.controller.MovieController;
import saeed.trivago.casestudy.listener.EndlessRecyclerViewScrollListener;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.SearchedMovie;
import saeed.trivago.casestudy.modeladapter.MovieAdapter;
import saeed.trivago.casestudy.modeladapter.SearchMovieAdapter;
import saeed.trivago.casestudy.util.Utilities;

/**
 * Created by saeed on 11/07/2016.
 */
public class MovieActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private MovieController mMovieController;
    private RecyclerView mMovieList;
    private ArrayList<Movie> mMovies;
    private ArrayList<SearchedMovie> mSearchedMoviesList;
    private SearchMovieAdapter mSearchMovieAdapter;
    private MovieAdapter mMovieAdapter;
    private View mLoadingMovies;
    private View mUpdateMoviesLoader;
    private TextView mEmptyList;
    private View mEmptyView;
    private RecyclerView mSearchedMovies;
    private String mSearchText = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);
        mEmptyList = (TextView) findViewById(R.id.empty_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mLoadingMovies = findViewById(R.id.loading_movies);
        mEmptyView = findViewById(R.id.empty_layout);
        mUpdateMoviesLoader = findViewById(R.id.update_movie_loader);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMovieController = (MovieController) fragmentManager.findFragmentByTag(MovieController.class.getSimpleName());
        if (mMovieController == null) {
            fragmentManager.beginTransaction().add(mMovieController = new MovieController(), MovieController.class.getSimpleName()).commit();
        }

        mSearchedMovies = (RecyclerView) findViewById(R.id.searched_movies);
        mMovieList = (RecyclerView) findViewById(R.id.movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMovieList.setLayoutManager(gridLayoutManager);
        mMovieList.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showUpdateLoader(true);
                if (mSearchText.isEmpty()) {
                    mMovieController.getMoreMovies(String.valueOf(page));
                } else {
                    mMovieController.searchMovie(mSearchText, String.valueOf(page));
                }
            }
        });

        mSearchedMovies.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mSearchedMovies.getLayoutManager()) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showUpdateLoader(true);
                if (mSearchText.isEmpty()) {
                    mMovieController.getMoreMovies(String.valueOf(page));
                } else {
                    mMovieController.searchMovie(mSearchText, String.valueOf(page));
                }
            }
        });

        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        mMovieList.setItemAnimator(animator);

        if (Utilities.isNetworkAvailable(this)) {
            mLoadingMovies.setVisibility(View.GONE);
            mMovieController.getPopularMovies("1");
        } else {
            Utilities.updateDisplayForError(this);
        }
    }

    private void showUpdateLoader(boolean show) {
        mUpdateMoviesLoader.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mSearchText = "";
                showRecyclerView(false);
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(true);
        searchView.setSubmitButtonEnabled(false);
        searchView.setImeOptions(0);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MovieActivity.class)));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    private void showRecyclerView(boolean search) {
        if (search) {
            mSearchedMovies.setVisibility(View.VISIBLE);
            mMovieList.setVisibility(View.GONE);
        } else {
            Log.v("Called", "Called 1");
            mSearchedMovies.setVisibility(View.GONE);
            mMovieList.setVisibility(View.VISIBLE);
        }
        mLoadingMovies.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void setUpPopularMovies(ArrayList<Movie> movies) {
        if (!movies.isEmpty()) {
            if (mMovies == null) {
                mMovies = new ArrayList<>(movies);
                mMovieAdapter = new MovieAdapter(mMovies);
                mMovieList.setAdapter(new AlphaInAnimationAdapter(mMovieAdapter));
                setVisibilityOfMovieLoader();
            } else {
                int lastPosition = mMovies.size();
                mMovies.addAll(movies);
                int lastPositionAfterAdd = mMovies.size();
                mMovieAdapter.notifyItemRangeInserted(lastPosition, lastPositionAfterAdd - 1);
                showUpdateLoader(false);
            }
        } else {
            setEmptyListText(getString(R.string.no_movies));
        }
    }

    public void setUpSearchedMovies(ArrayList<SearchedMovie> movies) {
        if (!movies.isEmpty()) {
            if (mSearchedMoviesList == null) {
                mSearchedMoviesList = new ArrayList<>(movies);
                mSearchMovieAdapter = new SearchMovieAdapter(mSearchedMoviesList);
                mSearchedMovies.setAdapter(new AlphaInAnimationAdapter(mSearchMovieAdapter));
                setVisibilityOfMovieLoaderSearch();
            } else {
                int lastPosition = mSearchedMoviesList.size();
                mSearchedMoviesList.addAll(movies);
                int lastPositionAfterAdd = mSearchedMoviesList.size();
                mSearchMovieAdapter.notifyItemRangeInserted(lastPosition, lastPositionAfterAdd - 1);
                showUpdateLoader(false);
            }
            hideEmptyView();

        } else {
            setEmptyListText(getString(R.string.no_movies));
        }
    }

    private void hideEmptyView() {
        if (mEmptyView.getVisibility() == View.VISIBLE) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void setEmptyListText(String text) {
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyList.setText(text);
        mMovieList.setVisibility(View.GONE);
        mLoadingMovies.setVisibility(View.GONE);
    }


    private void setVisibilityOfMovieLoader() {
        Utilities.slideViewFromRight(mLoadingMovies, mMovieList, 500);
    }

    private void setVisibilityOfMovieLoaderSearch() {
        Utilities.slideViewFromRight(mLoadingMovies, mSearchedMovies, 500);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchedMoviesList = null;
        mSearchText = newText;
        if (!mSearchText.trim().isEmpty()) {
            if (mMovieList.getVisibility() == View.VISIBLE) {
                showRecyclerView(true);
            }
            mMovieController.searchMovie("01", mSearchText);
        } else {
            if (mMovieList.getVisibility() != View.VISIBLE && mSearchedMovies.getVisibility() != View.VISIBLE) {
                hideEmptyView();
                mMovieList.setVisibility(View.VISIBLE);
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMovieController != null) {
            mMovieController.cancelRequestPopular();
        }
    }

}
