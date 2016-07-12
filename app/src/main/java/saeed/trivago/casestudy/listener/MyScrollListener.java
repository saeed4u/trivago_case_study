package saeed.trivago.casestudy.listener;

import android.widget.AbsListView;

/**
 * An infinite scroll listener for a {@code ListView}
 * Created by saeed on 11/07/2016.
 */
public abstract class MyScrollListener implements AbsListView.OnScrollListener {

    private int mVisibleThreshold = 0;
    private int mCurrentPage = 0;
    private int mPreviousTotal = 0;
    private boolean isLoading = true;


    public abstract void loadMoreMovies(int page);

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (isLoading) {
            if (totalItemCount > mPreviousTotal) {
                isLoading = false;
                mPreviousTotal = totalItemCount;
                mCurrentPage++;
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            loadMoreMovies(mCurrentPage + 1);
            isLoading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
