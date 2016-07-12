package saeed.trivago.casestudy.modeladapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.modelviewholder.MovieViewHolder;

/**
 * Created by saeed on 7/11/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private ArrayList<Movie> mMovies;

    public MovieAdapter(ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bindData(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
