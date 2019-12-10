package com.twinedo.themoviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.twinedo.themoviecatalogue.reminder.DailyReceiver;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.reminder.ReleaseReceiver;
import com.twinedo.themoviecatalogue.api.MovieService;
import com.twinedo.themoviecatalogue.api.RestAPI;
import com.twinedo.themoviecatalogue.object.Movie;
import com.twinedo.themoviecatalogue.object.MovieResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.twinedo.themoviecatalogue.BuildConfig.API_KEY;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    private DailyReceiver dailyAlarm = new DailyReceiver();
    private ReleaseReceiver dailyRelease = new ReleaseReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout lineLanguage = findViewById(R.id.lineLanguage);
        SwitchCompat switch_release = findViewById(R.id.switch_release);
        SwitchCompat switch_daily = findViewById(R.id.switch_daily);
        final Toolbar toolbar = findViewById(R.id.setting_toolbar);

        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lineLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            }
        });

        Context context = SettingActivity.this;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("DailySwitch", Context.MODE_PRIVATE);

        boolean switchDaily = sharedPreferences.getBoolean("savedDaily", false);
        boolean switchRelease = sharedPreferences.getBoolean("savedRelease", false);

        if (!switch_daily.isChecked()) {
            switch_daily.setChecked(switchDaily);
        } else {
            switch_daily.setChecked(true);
        }

        if (!switch_release.isChecked()) {
            switch_release.setChecked(switchRelease);
        } else {
            switch_release.setChecked(true);
        }

        switch_daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("savedDaily", false);
                    editor.apply();

                    Log.e(TAG, "Tombol tidak di cek");
                    dailyAlarm.cancelAlarm(SettingActivity.this, DailyReceiver.TYPE_REPEATING);
                } else {
                    Log.e(TAG, "Tombol di cek");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("savedDaily", true);
                    editor.apply();

                    String repeatTime = "07:00";
                    String repeatMessage = getResources().getString(R.string.daily_reminder_message);

                    dailyAlarm.setRepeatingAlarm(SettingActivity.this, DailyReceiver.TYPE_REPEATING,
                            repeatTime, repeatMessage);
                }
            }
        });

        switch_release.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("savedRelease", false);
                    editor.apply();

                    Log.e(TAG, "Tombol tidak di cek");
                    dailyRelease.cancelAlarm(SettingActivity.this, ReleaseReceiver.TYPE_REPEATING);
                } else {
                    Log.e(TAG, "Tombol di cek");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("savedRelease", true);
                    editor.apply();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String releaseDate1 = simpleDateFormat.format(date);
                    String releaseDate2 = simpleDateFormat.format(date);

                    setReleaseMovie(releaseDate1, releaseDate2);
                }
            }
        });
    }

    private void setReleaseMovie(final String releaseDate1, String releaseDate2) {
        final MovieService movieService = RestAPI.getRetrofit().create(MovieService.class);
        Call<MovieResult> call = movieService.getReleaseToday(API_KEY,
                releaseDate1,
                releaseDate2);

        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body() != null){
                    ArrayList<Movie> releaseToday;
                    releaseToday = response.body().results;

                    Gson gson = new Gson();
                    String json = gson.toJson(releaseToday);
                    Log.e(TAG, "datanya: " + releaseToday);
                    Log.e(TAG, "jsonnya: " + json);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String releaseTodayBanget = simpleDateFormat.format(date);

                    if (releaseToday != null) {
                        for (int i = 0; i < releaseToday.size(); i++) {
                            if (releaseToday.get(i).getRelease_date().equals(releaseTodayBanget)) {

                                String release = releaseToday.get(0).getTitle();
                                if (release != null){
                                    String repeatTime = "08:00";

                                    Log.e(TAG, "releasan baru nih: " + release);
                                    dailyRelease.setRepeatingRelease(SettingActivity.this, ReleaseReceiver.TYPE_REPEATING,
                                            repeatTime, release);
                                } else {
                                    String repeatTime = "08:00";
                                    String noMovie = "";

                                    Log.e(TAG, "tidak ada movie baru hari ini ");
                                    dailyRelease.setRepeatingRelease(SettingActivity.this, ReleaseReceiver.TYPE_REPEATING,
                                            repeatTime, noMovie);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                Log.e(TAG, "failed: ");
            }
        });
    }
}
