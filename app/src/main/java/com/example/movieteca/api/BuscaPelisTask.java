package com.example.movieteca.api;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.movieteca.BuildConfig;
import com.example.movieteca.model.Pelicula;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuscaPelisTask extends AsyncTask<String,Void, Pelicula[]> {
    private final PelisCallback movieTaskCallback;

    public BuscaPelisTask(PelisCallback movieTaskCallback) {
        this.movieTaskCallback = movieTaskCallback;
    }

    @Contract("null -> null")
    private Pelicula[] getMoviesFromJson(String movieJsonString) throws JSONException {
        final String MOVIE_ID="id";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        if (movieJsonString == null || "".equals(movieJsonString)) {
            return null;
        }

        JSONObject jsonObjectMovie = new JSONObject(movieJsonString);
        JSONArray jsonArrayMovies = jsonObjectMovie.getJSONArray("results");

        Pelicula[] movies = new Pelicula[jsonArrayMovies.length()];

        for (int i = 0; i < jsonArrayMovies.length(); i++) {
            JSONObject object = jsonArrayMovies.getJSONObject(i);
            movies[i] = new Pelicula(
                    object.getInt(MOVIE_ID),
                    object.getString(TITLE),
                    object.getString(POSTER_PATH),
                    object.getString(OVERVIEW),
                    object.getString(VOTE_AVERAGE),
                    object.getString(RELEASE_DATE));
        }
        return movies;

    }
    @Override
    protected Pelicula[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        final String BASE_URL = "https://api.themoviedb.org/3/";
        final String API_KEY = "api_key";
        Uri uri;

        if (params[0].equals("popular")||params[0].equals("top_rated")){
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath("movie/")
                    .appendEncodedPath(params[0])
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    .appendQueryParameter("language","es-ES")
                    .build();
        }else {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath("search/movie")
                    .appendQueryParameter("query",params[0])
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    .appendQueryParameter("language","es-ES")
                    .build();

        }



        String jsonString = NetworkRequest.getJsonString(uri);

        try {
            return getMoviesFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Pelicula[] movies) {
        movieTaskCallback.updateAdapter(movies);
    }
}
