package com.twinedo.themoviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.image.ImageSize;
import com.twinedo.themoviecatalogue.object.Movie;

import java.util.concurrent.ExecutionException;

import static com.twinedo.themoviecatalogue.api.ServiceURL.IMAGE_URL;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.CONTENT_URI_MOVIE;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = StackRemoteViewsFactory.class.getSimpleName();

    private final Context mContext;
    private Cursor cursor;
    private Bitmap bitmap;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        cursor = mContext.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Movie movie = getPosterMovie(position);

        String poster = IMAGE_URL+ ImageSize.w500.getValue() + movie.getPoster_path();

        Log.e(TAG, "posterString : " + poster);
        try {
            bitmap = Glide.with(mContext).asBitmap().load(poster).submit().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);


        return rv;
    }

    private Movie getPosterMovie(int position) {
        cursor.moveToPosition(position);
        return new Movie(cursor);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}