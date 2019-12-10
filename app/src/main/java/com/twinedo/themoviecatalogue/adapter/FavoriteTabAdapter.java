package com.twinedo.themoviecatalogue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.twinedo.themoviecatalogue.fragment.FavMovieFragment;
import com.twinedo.themoviecatalogue.fragment.FavTVFragment;

public class FavoriteTabAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    public FavoriteTabAdapter(FragmentManager fm, int numTabs) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numTabs = numTabs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FavMovieFragment tabFavMovies = new FavMovieFragment();
                notifyDataSetChanged();
                return tabFavMovies;
            case 1:
                FavTVFragment tabFavTV= new FavTVFragment();
                notifyDataSetChanged();
                return tabFavTV;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
