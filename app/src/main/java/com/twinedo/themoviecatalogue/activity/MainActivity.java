package com.twinedo.themoviecatalogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.adapter.CatalogueAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewPager;
    private int[] tabIcon = {R.drawable.ic_movie_white_24dp, R.drawable.ic_live_tv_white_24dp };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView settings = findViewById(R.id.settings);
        ImageView favorite = findViewById(R.id.favorite);
        SearchView searchMovie = findViewById(R.id.searchMovie);

        searchMovie.setQueryHint(getString(R.string.searchView));

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.movies).setIcon(tabIcon[0]));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tvShows).setIcon(tabIcon[1]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CatalogueAdapter catalogueAdapter = new CatalogueAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(catalogueAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        searchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("query", query);

                    Bundle bundle = new Bundle();
                    bundle.putString("query", query);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.e(TAG, "isi search: " + query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFav = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(gotoFav);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(goToSettings);
            }
        });
    }
}
