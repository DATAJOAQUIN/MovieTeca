package com.example.movieteca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.movieteca.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 1;

    public static final String LOGTAG = "FAVORITE";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.NOMBRE_TABLA + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA + " INTEGER, " +
                FavoriteContract.FavoriteEntry.COLUMN_TITULO + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_VALORACION + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.NOMBRE_TABLA);
        onCreate(sqLiteDatabase);

    }

    public void addFavorite(Pelicula movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA, movie.getMovieId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITULO, movie.getTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_VALORACION, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER, movie.getPosterPath());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS, movie.getOverview());

        db.insert(FavoriteContract.FavoriteEntry.NOMBRE_TABLA, null, values);
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.NOMBRE_TABLA, FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA + "=" + id, null);
    }

    public boolean searchFavorite(int id){
         Pelicula movie=null;
        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA,
                FavoriteContract.FavoriteEntry.COLUMN_TITULO,
                FavoriteContract.FavoriteEntry.COLUMN_VALORACION,
                FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER,
                FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS

        };

        String[] selectionArgs={Integer.toString(id)};

        String sortOrder =FavoriteContract.FavoriteEntry._ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM "+FavoriteContract.FavoriteEntry.NOMBRE_TABLA+" WHERE  "+FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA+"=?",selectionArgs);


        while(cursor.moveToNext()){
                movie = new Pelicula();
                movie.setMovieId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITULO)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VALORACION)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS)));

            }

        cursor.close();

        if (movie!=null){
            return true;
        }
        return false;
    }

    public List<Pelicula> getAllFavorite() {
        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA,
                FavoriteContract.FavoriteEntry.COLUMN_TITULO,
                FavoriteContract.FavoriteEntry.COLUMN_VALORACION,
                FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER,
                FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS

        };
        String sortOrder =
                FavoriteContract.FavoriteEntry._ID + " ASC";
        List<Pelicula> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.NOMBRE_TABLA,
                null,
                null,
                null,
                null,
                null,
                sortOrder);

            while(cursor.moveToNext()) {
                Pelicula movie = new Pelicula();
                movie.setMovieId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_ID_PELICULA))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITULO)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VALORACION)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RUTA_POSTER)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_SINOPSIS)));

                favoriteList.add(movie);
            }

        cursor.close();

        return favoriteList;
    }
}