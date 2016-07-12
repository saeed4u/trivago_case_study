package saeed.trivago.casestudy.modeladapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.SearchedMovie;
import saeed.trivago.casestudy.modelviewholder.SearchMovieViewHolder;

/**
 * Created by saeed on 12/07/2016.
 */
public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieViewHolder> {

    private ArrayList<SearchedMovie> mMovies;

    public SearchMovieAdapter(ArrayList<SearchedMovie> mMovies) {
        this.mMovies = mMovies;
    }

    @Override
    public SearchMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchMovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_search_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchMovieViewHolder holder, int position) {
        SearchedMovie movie = mMovies.get(position);
        holder.bindData(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
