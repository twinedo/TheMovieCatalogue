package com.twinedo.themoviecatalogue.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.twinedo.themoviecatalogue.LoadMoviesCallback;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.activity.DetailMovieActivity;
import com.twinedo.themoviecatalogue.adapter.FavoriteMovieAdapter;
import com.twinedo.themoviecatalogue.object.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.CONTENT_URI_MOVIE;
import static com.twinedo.themoviecatalogue.helper.MappingHelper.mapCursorMovieToArrayList;

public class FavMovieFragment extends Fragment implements LoadMoviesCallback {

    private FavoriteMovieAdapter favoriteMovieAdapter = new FavoriteMovieAdapter();

    private ProgressBar loadingBar;
    private TextView textFavMovie;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle(R.string.fav_mov);
        }

        loadingBar = view.findViewById(R.id.loading);
        textFavMovie = view.findViewById(R.id.textFavMovie);
        textFavMovie.setText(R.string.textFavMovie);

        showLoading(true);

        RecyclerView rvFavMovies = view.findViewById(R.id.rv_fav_movies);
        rvFavMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavMovies.setHasFixedSize(true);
        rvFavMovies.setAdapter(favoriteMovieAdapter);
        favoriteMovieAdapter.notifyDataSetChanged();
        favoriteMovieAdapter.setOnClickListener(new FavoriteMovieAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(long id, Movie movie) {
                gotoDetail(id, movie);
                if (getActivity() != null){
                    getActivity().finish();
                }
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver movieObserver = new DataObserver(handler, getContext());
        if (getContext() != null)
        getContext().getContentResolver()
                .registerContentObserver(CONTENT_URI_MOVIE, true, movieObserver);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(getContext(), this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            showLoading(false);
            assert list != null;
            if (list.isEmpty()) {
                textFavMovie.setVisibility(View.VISIBLE);
            } else {
                favoriteMovieAdapter.setListMovie(list);
                textFavMovie.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteMovieAdapter.getListMovie());
    }

    @Override
    public void preExecute() {
        showLoading(true);
    }

    @Override
    public void postExecute(Cursor movies) {
        showLoading(false);

        ArrayList<Movie> listMovies = mapCursorMovieToArrayList(movies);
        if (listMovies.size()> 0) {
            favoriteMovieAdapter.setListMovie(listMovies);
            textFavMovie.setVisibility(View.GONE);
        } else {
            favoriteMovieAdapter.setListMovie(new ArrayList<Movie>());
            textFavMovie.setVisibility(View.VISIBLE);
        }
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);

        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            loadingBar.setVisibility(View.VISIBLE);
        } else {
            loadingBar.setVisibility(View.GONE);
        }
    }

    private void gotoDetail(long id, Movie movie){
        String data = new Gson().toJson(movie);

        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("movie_id", id);
        intent.putExtra("movie_data", data);
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMoviesAsync(context, (LoadMoviesCallback) context).execute();
        }
    }
}
