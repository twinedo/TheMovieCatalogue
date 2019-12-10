package com.twinedo.themoviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.twinedo.themoviecatalogue.object.Movie;
import com.twinedo.themoviecatalogue.object.TvShows;

import static android.provider.BaseColumns._ID;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.RELEASE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.VOTE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_MOVIE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_TV;

public class FavoriteHelper {

    private static final String DATABASE_TABLE_MOVIE = TABLE_FAVORITE_MOVIE;
    private static final String DATABASE_TABLE_TV = TABLE_FAVORITE_TV;
    private final DBHelper dbHelper;
    private static FavoriteHelper INSTANCE;

    private SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /*public void close() {
        dbHelper.close();
        if (database.isOpen())
            database.close();
    }*/

    /*public ArrayList<Movie> getAllFavoriteMovie() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0 ) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }*/

    public Movie getFavoriteMovieById(long id ) {
        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                null,
                _ID+" = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Movie movie = new Movie();
        if (cursor.getCount() > 0 ) {
            do {
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return movie;
    }

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(POSTER, movie.getPoster_path());
        if (movie.getBackdrop_path() == null) {
            args.put(BACKDROP, movie.getPoster_path());
        } else {
            args.put(BACKDROP, movie.getBackdrop_path());
        }
        args.put(RELEASE, movie.getRelease_date());
        args.put(VOTE, movie.getVote_average());
        args.put(OVERVIEW, movie.getOverview());
        return database.insert(DATABASE_TABLE_MOVIE, null, args);
    }

    public void deleteMovie(long id) {
        database.delete(DATABASE_TABLE_MOVIE, _ID + " = '" + id + "'", null);
    }

    /*public ArrayList<TvShows> getAllFavoriteTV() {
        ArrayList<TvShows> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_TV,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShows tvShows;
        if (cursor.getCount() > 0 ) {
            do {
                tvShows = new TvShows();
                tvShows.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvShows.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShows.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                tvShows.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                tvShows.setFirst_air_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                tvShows.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                tvShows.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(tvShows);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }*/

    public TvShows getFavoriteTVById(long id ) {
        Cursor cursor = database.query(DATABASE_TABLE_TV,
                null,
                _ID+" = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        TvShows tvShows = new TvShows();
        if (cursor.getCount() > 0 ) {
            do {
                tvShows.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvShows.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShows.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                tvShows.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                tvShows.setFirst_air_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                tvShows.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                tvShows.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return tvShows;
    }

    public long insertTV(TvShows tvShows) {
        ContentValues args = new ContentValues();
        args.put(_ID, tvShows.getId());
        args.put(TITLE, tvShows.getName());
        args.put(POSTER, tvShows.getPoster_path());
        args.put(BACKDROP, tvShows.getBackdrop_path());
        args.put(RELEASE, tvShows.getFirst_air_date());
        args.put(VOTE, tvShows.getVote_average());
        args.put(OVERVIEW, tvShows.getOverview());
        return database.insert(DATABASE_TABLE_TV, null, args);
    }

    public void deleteTV(long id) {
        database.delete(DATABASE_TABLE_TV, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProviderMovie(String id) {
        return database.query(DATABASE_TABLE_MOVIE, null
        , _ID + " = ?"
        , new String[]{id}
        , null
        , null
        , null
        , null);
    }

    public Cursor queryProviderMovie() {
        return database.query(DATABASE_TABLE_MOVIE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    /*public long insertProviderMovie(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }*/

    /*public int updateProviderMovie(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_MOVIE, values, _ID + " = ?", new String[]{id});
    }*/

    public int deleteProviderMovie(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, _ID + " = '"+ id+"'", new String[]{id});
    }


    public Cursor queryByIdProviderTV(String id) {
        return database.query(DATABASE_TABLE_TV, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProviderTV() {
        return database.query(DATABASE_TABLE_TV
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    /*public long insertProviderTV(ContentValues values) {
        return database.insert(DATABASE_TABLE_TV, null, values);
    }*/

    /*public int updateProviderTV(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_TV, values, _ID + " = ?", new String[]{id});
    }*/

    public int deleteProviderTV(String id) {
        return database.delete(DATABASE_TABLE_TV, _ID + " = '"+ id+"'", new String[]{id});
    }
}
