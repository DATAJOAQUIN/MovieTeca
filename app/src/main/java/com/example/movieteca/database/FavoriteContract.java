package com.example.movieteca.database;

import android.provider.BaseColumns;

public final class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns {

        public static final String NOMBRE_TABLA = "favorite";
        public static final String COLUMN_ID_PELICULA= "movieid";
        public static final String COLUMN_TITULO = "title";
        public static final String COLUMN_VALORACION = "userrating";
        public static final String COLUMN_RUTA_POSTER = "posterpath";
        public static final String COLUMN_SINOPSIS = "overview";
    }
}
