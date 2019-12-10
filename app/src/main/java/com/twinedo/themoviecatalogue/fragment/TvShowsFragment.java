package com.twinedo.themoviecatalogue.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.twinedo.themoviecatalogue.TvViewModel;
import com.twinedo.themoviecatalogue.activity.DetailTvShowsActivity;
import com.twinedo.themoviecatalogue.adapter.ListTvShowsAdapter;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.object.TvShows;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment {

    private ListTvShowsAdapter listTvShowsAdapter = new ListTvShowsAdapter();
    private ProgressBar loadingBar;

    public TvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingBar = view.findViewById(R.id.loading);

        TvViewModel model = ViewModelProviders.of(this).get(TvViewModel.class);
        model.getTvShows().observe(this, getTvShows);

        showLoading(true);

        listTvShowsAdapter.notifyDataSetChanged();

        RecyclerView rvTvShows = view.findViewById(R.id.rv_tvshows);
        rvTvShows.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTvShows.setAdapter(listTvShowsAdapter);

        listTvShowsAdapter.setOnClickListener(new ListTvShowsAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(long id, TvShows tvShows) {
                gotoDetail(id, tvShows);
            }
        });
    }

    private Observer<ArrayList<TvShows>> getTvShows = new Observer<ArrayList<TvShows>>() {
        @Override
        public void onChanged(ArrayList<TvShows> tvShows) {
            if (tvShows != null ){
                showLoading(false);
                listTvShowsAdapter.setListTvShows(tvShows);
            } else {
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

    private void gotoDetail(long id, TvShows tvShows) {
        String data = new Gson().toJson(tvShows);

        Intent intent = new Intent(getActivity(), DetailTvShowsActivity.class);
        intent.putExtra("tv_id", id);
        intent.putExtra("tv_data", data);
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }
}
