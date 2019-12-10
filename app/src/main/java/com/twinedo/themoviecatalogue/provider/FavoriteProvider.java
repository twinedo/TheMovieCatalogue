package com.twinedo.themoviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.twinedo.themoviecatalogue.db.FavoriteHelper;
import com.twinedo.themoviecatalogue.fragment.FavMovieFragment;
import com.twinedo.themoviecatalogue.fragment.FavTVFragment;

import static com.twinedo.themoviecatalogue.db.DatabaseContract.AUTHORITY;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.CONTENT_URI_MOVIE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.CONTENT_URI_TV;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_MOVIE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_TV;

public class FavoriteProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;


    static {
        // content://com.twinedo.themoviecatalogue/movie
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE, MOVIE);
        // content://com.twinedo.themoviecatalogue/movie/id
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE + "/#", MOVIE_ID);
        // content://com.twinedo.themoviecatalogue/tvshows
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TV, TVSHOW);
        // content://com.twinedo.themoviecatalogue/tvshows/id
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TV + "/#", TVSHOW_ID);
    }
    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryProviderMovie();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryByIdProviderMovie(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = favoriteHelper.queryProviderTV();
                break;
            case TVSHOW_ID:
                cursor = favoriteHelper.queryByIdProviderTV(uri.getLastPathSegment());
                break;
                default:
                    cursor = null;
                    break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        /*favoriteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favoriteHelper.insertProviderMovie(contentValues);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                            new FavMovieFragment.DataObserver(new android.os.Handler(), getContext()));

                break;
            case TVSHOW:
                added = favoriteHelper.insertProviderTV(contentValues);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                            new FavTVFragment.DataObserver(new android.os.Handler(), getContext()));
                break;
                default:
                    added = 0;
                    break;
        }*/

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favoriteHelper.deleteProviderMovie(uri.getLastPathSegment());
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                            new FavMovieFragment.DataObserver(new android.os.Handler(), getContext()));
                break;
            case TVSHOW_ID:
                deleted = favoriteHelper.deleteProviderTV(uri.getLastPathSegment());
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                            new FavTVFragment.DataObserver(new android.os.Handler(), getContext()));
                break;
                default:
                    deleted = 0;
                    break;
        }


        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        /*favoriteHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = favoriteHelper.updateProviderMovie(uri.getLastPathSegment(), contentValues);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                            new FavMovieFragment.DataObserver(new android.os.Handler(), getContext()));
                break;
            case TVSHOW_ID:
                updated = favoriteHelper.updateProviderTV(uri.getLastPathSegment(), contentValues);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                            new FavTVFragment.DataObserver(new android.os.Handler(), getContext()));
                break;
                default:
                    updated = 0;
                    break;
        }*/
        return 0;
    }
}
