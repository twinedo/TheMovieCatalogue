package com.twinedo.themoviecatalogue.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.twinedo.themoviecatalogue.MovieViewModel;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.activity.DetailMovieActivity;
import com.twinedo.themoviecatalogue.adapter.ListMovieAdapter;
import com.twinedo.themoviecatalogue.object.Movie;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private ListMovieAdapter listMovieAdapter = new ListMovieAdapter();

    private ProgressBar loadingBar;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingBar = view.findViewById(R.id.loading);
        TextView textNoSearch = view.findViewById(R.id.textNoSearch);
        textNoSearch.setVisibility(View.GONE);

        MovieViewModel model = ViewModelProviders.of(this).get(MovieViewModel.class);
        model.getMovies().observe(this, getMovies);

        showLoading(true);

        RecyclerView rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovies.setAdapter(listMovieAdapter);

        listMovieAdapter.notifyDataSetChanged();
        listMovieAdapter.setOnClickListener(new ListMovieAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(long id, Movie movie) {
                    gotoDetail(id, movie);
            }
        });
    }

    private Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                showLoading(false);
                listMovieAdapter.setListMovies(movies);
            } else {
                showLoading(false);
                AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(getResources().getString(R.string.error));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed();
                    }
                });
                alertDialog.show();
            }
        }
    };

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
}
