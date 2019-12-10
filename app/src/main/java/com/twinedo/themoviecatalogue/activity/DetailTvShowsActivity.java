package com.twinedo.themoviecatalogue.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.TvViewModel;
import com.twinedo.themoviecatalogue.adapter.ListCreditsAdapter;
import com.twinedo.themoviecatalogue.adapter.ListTrailersAdapter;
import com.twinedo.themoviecatalogue.api.ServiceURL;
import com.twinedo.themoviecatalogue.db.FavoriteHelper;
import com.twinedo.themoviecatalogue.object.Credits;
import com.twinedo.themoviecatalogue.object.Genres;
import com.twinedo.themoviecatalogue.object.Trailer;
import com.twinedo.themoviecatalogue.object.TvShows;
import com.twinedo.themoviecatalogue.image.ImageParse;
import com.twinedo.themoviecatalogue.image.ImageSize;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DetailTvShowsActivity extends AppCompatActivity implements View.OnClickListener {

    // public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    // public static final String TAG = DetailTvShowsActivity.class.getSimpleName();

    private ListTrailersAdapter listTrailersAdapter = new ListTrailersAdapter();
    private ListCreditsAdapter listCreditsAdapter = new ListCreditsAdapter();

    private ImageView detailPhoto;
    private ImageView detailBackdrop;
    private ImageView favorite_btn;
    private ImageView delete_btn;

    private TextView detailTitle;
    private TextView detailRelease;
    private TextView detailRate;
    private TextView detailOverview;
    private TextView detailGenre;

    private ProgressBar detailLoadingBar;
    private RatingBar detailRatingBar;

    private TvShows tvShows;
    private long id;
    public int position;

    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        id = getIntent().getLongExtra("tv_id", -1);
        String tvData = getIntent().getStringExtra("tv_data");

        tvShows = new Gson().fromJson(tvData, TvShows.class);

        detailPhoto = findViewById(R.id.mov_photo);
        detailBackdrop = findViewById(R.id.mov_backdrop);
        detailTitle = findViewById(R.id.mov_title);
        detailRelease = findViewById(R.id.mov_date);
        detailRate = findViewById(R.id.mov_rate);
        detailOverview = findViewById(R.id.mov_overview);
        detailGenre = findViewById(R.id.mov_genre);
        detailLoadingBar = findViewById(R.id.loading);
        detailRatingBar = findViewById(R.id.mov_bar);
        favorite_btn = findViewById(R.id.favorite_btn);
        delete_btn = findViewById(R.id.delete_btn);

        RecyclerView recyclerViewTrailer = findViewById(R.id.recyclerview_trailer);
        RecyclerView recyclerViewCredit = findViewById(R.id.recyclerview_credits);
        ImageView back = findViewById(R.id.back);
        ImageView share = findViewById(R.id.share);

        TvViewModel tvViewModel = ViewModelProviders.of(this).get(TvViewModel.class);
        tvViewModel.getTvShows().observe(this, getTv);
        tvViewModel.getTrailers(id).observe(this, getTrailer);
        tvViewModel.getCredits(id).observe(this, getCredit);
        tvViewModel.getGenres(id).observe(this, getGenre);

        showLoading(true);

        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailer.setNestedScrollingEnabled(false);
        recyclerViewTrailer.setAdapter(listTrailersAdapter);

        listTrailersAdapter.notifyDataSetChanged();
        listTrailersAdapter.setOnClickListener(new ListTrailersAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(String key,
                                            Trailer movie) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServiceURL.BASE_VIDEO_URL + key));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        listCreditsAdapter.notifyDataSetChanged();

        LinearLayoutManager linear2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCredit.setLayoutManager(linear2);
        recyclerViewCredit.setHasFixedSize(true);
        recyclerViewCredit.setAdapter(listCreditsAdapter);

        detailPhoto.setOnClickListener(this);
        share.setOnClickListener(this);
        back.setOnClickListener(this);
        favorite_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);

        if (tvShows != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        } else {
            tvShows = new TvShows();
        }

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        TvShows fav = favoriteHelper.getFavoriteTVById(tvShows.getId());
        if (fav.getId() != 0) {
            favorite_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.VISIBLE);
        } else {
            favorite_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.GONE);
        }
    }

    private Observer<ArrayList<TvShows>> getTv = new Observer<ArrayList<TvShows>>() {
        @Override
        public void onChanged(ArrayList<TvShows> tvShow) {
            if (tvShow != null) {
                showLoading(false);
                setTitle(null);

                detailTitle.setText(tvShows.getName());

                Uri uri = ImageParse.movieUrl(ImageSize.w500.getValue(), tvShows.getPoster_path());

                if (tvShows.getBackdrop_path() == null) {
                    Glide.with(DetailTvShowsActivity.this).load(uri).centerCrop().into(detailBackdrop);
                } else {
                    Uri uri2 = ImageParse.movieUrl(ImageSize.w500.getValue(), tvShows.getBackdrop_path());
                    Glide.with(DetailTvShowsActivity.this).load(uri2).centerCrop().into(detailBackdrop);
                }

                Glide.with(DetailTvShowsActivity.this).load(uri).centerCrop().into(detailPhoto);

                detailRatingBar.setRating(Float.parseFloat(String.valueOf(tvShows.getVote_average())));

                detailRelease.setText(tvShows.getFirst_air_date());
                detailRate.setText(String.valueOf(tvShows.getVote_average()));
                detailOverview.setText(tvShows.getOverview());

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailTvShowsActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(getResources().getString(R.string.error));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog.show();
            }
        }
    };

    private Observer<ArrayList<Trailer>> getTrailer = new Observer<ArrayList<Trailer>>() {
        @Override
        public void onChanged(ArrayList<Trailer> trailers) {
            if (trailers != null) {
                showLoading(false);
                listTrailersAdapter.setTrailerData(trailers);
            } else {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(DetailTvShowsActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(getResources().getString(R.string.error));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog.show();
            }
        }
    };

    private Observer<ArrayList<Credits>> getCredit = new Observer<ArrayList<Credits>>() {
        @Override
        public void onChanged(ArrayList<Credits> credits) {
            if (credits != null) {
                showLoading(false);
                listCreditsAdapter.setCreditsData(credits);
            } else {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(DetailTvShowsActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(getResources().getString(R.string.error));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog.show();
            }
        }
    };

    private Observer<ArrayList<Genres>> getGenre = new Observer<ArrayList<Genres>>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChanged(ArrayList<Genres> genres) {
            if (genres != null) {
                showLoading(false);

                JSONArray jsonArray = new JSONArray(genres);
                StringBuilder builder = new StringBuilder();
                for (int i=0 ; i <jsonArray.length(); i++) {
                    builder.append(genres.get(i).getName());
                    if (jsonArray.length()-1 != i) {
                        builder.append(", ");
                    }
                }

                detailGenre.setText(builder.toString());

            } else {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(DetailTvShowsActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(getResources().getString(R.string.error));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog.show();
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            detailLoadingBar.setVisibility(View.VISIBLE);
        } else {
            detailLoadingBar.setVisibility(View.GONE);
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mov_photo:
                tvShows.setPoster_path(tvShows.getPoster_path());

                Intent imgHeader = new Intent(DetailTvShowsActivity.this, ImgHeaderTvActivity.class);
                imgHeader.putExtra(ImgHeaderTvActivity.EXTRA_PHOTO, tvShows);
                startActivity(imgHeader);
                break;
            case R.id.share:
                Bitmap bitmap = getBitmapFromView(detailPhoto);
                try {
                    File file = new File(getExternalCacheDir(), "*.PNG");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.toString()));
                    intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    intent.putExtra(Intent.EXTRA_TEXT, "Title: " + tvShows.getName() + "\n" +
                            "Release Date : " + tvShows.getFirst_air_date() + "\n" +
                            "Rating : " + tvShows.getVote_average() + "/10" +
                            "\nOverview :...." +
                            "\n" +
                            "\n" +
                            "get Movies Overview only on :" +
                            "\nhttps://www.themoviedb.org");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(intent, getText(R.string.share)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.favorite_btn:
                long result = favoriteHelper.insertTV(tvShows);

                if (result > 0) {
                    favorite_btn.setVisibility(View.GONE);
                    delete_btn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, R.string.addFav, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete_btn:
                favoriteHelper.deleteTV(id);
                favorite_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.GONE);

                Toast.makeText(this, R.string.delFav, Toast.LENGTH_SHORT).show();
        }
    }
}
