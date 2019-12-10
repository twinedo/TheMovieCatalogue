package com.twinedo.themoviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.RELEASE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.FavoriteColumns.VOTE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_MOVIE;
import static com.twinedo.themoviecatalogue.db.DatabaseContract.TABLE_FAVORITE_TV;

public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbFavorite";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVORITE_MOVIE =
            String.format("CREATE TABLE %s" + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s REAL NOT NULL," +
                    " %s TEXT NOT NULL)",
                    TABLE_FAVORITE_MOVIE,
                    _ID,
                    POSTER,
                    BACKDROP,
                    TITLE,
                    RELEASE,
                    VOTE,
                    OVERVIEW);

    private static final String SQL_CREATE_TABLE_FAVORITE_TV =
            String.format("CREATE TABLE %s" + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s REAL NOT NULL," +
                            " %s TEXT NOT NULL)",
                    TABLE_FAVORITE_TV,
                    _ID,
                    POSTER,
                    BACKDROP,
                    TITLE,
                    RELEASE,
                    VOTE,
                    OVERVIEW);


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_TV);
        onCreate(sqLiteDatabase);
    }
}
