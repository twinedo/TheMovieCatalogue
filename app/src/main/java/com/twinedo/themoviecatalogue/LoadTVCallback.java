package com.twinedo.themoviecatalogue;

import android.database.Cursor;

public interface LoadTVCallback {
    void preExecute();

    void postExecute(Cursor tvShows);
}
