package com.twinedo.themoviecatalogue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.twinedo.themoviecatalogue.fragment.MoviesFragment;
import com.twinedo.themoviecatalogue.fragment.TvShowsFragment;

public class CatalogueAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    public CatalogueAdapter(FragmentManager fm, int numTabs) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MoviesFragment tabMovies = new MoviesFragment();
                notifyDataSetChanged();
                return tabMovies;
            case 1:
                TvShowsFragment tabTVShows = new TvShowsFragment();
                notifyDataSetChanged();
                return tabTVShows;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

}
