package com.twinedo.themoviecatalogue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.twinedo.themoviecatalogue.fragment.SearchMoviesFragment;
import com.twinedo.themoviecatalogue.fragment.SearchTVFragment;

public class SearchAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    public SearchAdapter(FragmentManager fm, int numTabs) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numTabs = numTabs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchMoviesFragment tabSearchMovies = new SearchMoviesFragment();
                notifyDataSetChanged();
                return tabSearchMovies;
            case 1:
                SearchTVFragment tabSearchTV = new SearchTVFragment();
                notifyDataSetChanged();
                return tabSearchTV;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
