package com.twinedo.themoviecatalogue.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.adapter.SearchAdapter;

public class SearchActivity extends AppCompatActivity {

    // private long id;
    private int[] tabIcon = {R.drawable.ic_movie_white_24dp, R.drawable.ic_live_tv_white_24dp };
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Toolbar toolbar = findViewById(R.id.search_toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.movies).setIcon(tabIcon[0]));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tvShows).setIcon(tabIcon[1]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final String query = getIntent().getStringExtra("query");

        SearchAdapter searchAdapter = new SearchAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(searchAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        toolbar.setTitle(getString(R.string.searchMovResults) + " '"+query+"'");
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        toolbar.setTitle(getString(R.string.searchTVResults) + " '"+query+"'");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
