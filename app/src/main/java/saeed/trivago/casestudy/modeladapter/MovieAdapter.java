package saeed.trivago.casestudy.modeladapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import saeed.trivago.casestudy.R;
import saeed.trivago.casestudy.model.Movie;
import saeed.trivago.casestudy.model.MovieIds;

/**
 * Created by blanka on 7/11/2016.
 */
public class MovieAdapter extends BaseAdapter {

    private ArrayList<Movie> mMovies;
    private int mLastPosition = -1;

    public MovieAdapter(ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItemHolder viewItemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            viewItemHolder = new ViewItemHolder();
            viewItemHolder.movieImage = (ImageView) convertView.findViewById(R.id.movie_image);
            viewItemHolder.imdbId = (TextView) convertView.findViewById(R.id.imdb);
            viewItemHolder.tmdbId = (TextView) convertView.findViewById(R.id.tmdb);
            viewItemHolder.traktId = (TextView) convertView.findViewById(R.id.trakt);
            viewItemHolder.slugId = (TextView) convertView.findViewById(R.id.slug);
            viewItemHolder.movieTitle = (TextView) convertView.findViewById(R.id.movie_title);
            viewItemHolder.movieYear = (TextView) convertView.findViewById(R.id.movie_year);
            convertView.setTag(viewItemHolder);
        } else {
            viewItemHolder = (ViewItemHolder) convertView.getTag();
        }
        Movie movie = mMovies.get(position);
        Picasso.with(parent.getContext()).load(movie.getmLogoUrl()).into(viewItemHolder.movieImage);
        viewItemHolder.movieTitle.setText(movie.getmMovieTitle());
        viewItemHolder.movieYear.setText(movie.getmMovieYear());
        MovieIds movieIds = movie.getmMovieIds();
        viewItemHolder.imdbId.setText(movieIds.getmIMDBId());
        viewItemHolder.slugId.setText(movieIds.getmSludId());
        viewItemHolder.tmdbId.setText(movieIds.getmTmdbId());
        viewItemHolder.traktId.setText(movieIds.getmTraktId());

        Animation animation = AnimationUtils.loadAnimation(parent.getContext(), position > mLastPosition ? R.anim.up_from_bottom : R.anim.bottom_from_up);
        mLastPosition = position;
        convertView.startAnimation(animation);
        return convertView;
    }

    class ViewItemHolder {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieYear;
        TextView traktId;
        TextView slugId;
        TextView imdbId;
        TextView tmdbId;
    }
}
