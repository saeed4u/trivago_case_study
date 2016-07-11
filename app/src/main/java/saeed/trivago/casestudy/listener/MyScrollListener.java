package saeed.trivago.casestudy.listener;

import android.widget.AbsListView;

import static saeed.trivago.casestudy.util.AppConstants.NUMBER_OF_ITEMS_PER_REQUEST;

/**
 * An infinite scroll listener for a {@code ListView}
 * Created by saeed on 11/07/2016.
 */
public abstract class MyScrollListener implements AbsListView.OnScrollListener {

    // The current page of visible items
    private int mCurrentPageOfItems = 1;

    // Is movies being fetched
    private boolean isFetchingMovie = true;

    //Current number of movies in listview;
    private int mItemCount;

    public abstract void loadMoreMovies(int page);

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (mItemCount > i2) {
            mItemCount = i2;
            isFetchingMovie = mItemCount == 0;
        }

        if (isFetchingMovie && (i2 > mItemCount)) {
            isFetchingMovie = false;
            mItemCount = i2;
            mCurrentPageOfItems++;
        }

        if (!isFetchingMovie && (i2 - i1) <= (i + NUMBER_OF_ITEMS_PER_REQUEST)) {
            loadMoreMovies(mCurrentPageOfItems + 1);
            isFetchingMovie = true;
        }

    }
}
