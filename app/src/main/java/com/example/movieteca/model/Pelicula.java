package com.example.movieteca.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pelicula implements Parcelable {

    private  int movieId;
    private  String title;
    private  String posterPath;
    private  String overview;
    private  String voteAverage;
    private  String releaseDate;

    public Pelicula() {
    }

    public Pelicula(int movieId, String title, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.movieId=movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;

    }

    private Pelicula(Parcel parcel) {
        movieId = parcel.readInt();
        title = parcel.readString();
        posterPath = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readString();
        releaseDate = parcel.readString();

    }



    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }

    public static final Creator<Pelicula> CREATOR = new Creator<Pelicula>() {

        @Override
        public Pelicula createFromParcel(Parcel parcel) {
            return new Pelicula(parcel);
        }

        @Override
        public Pelicula[] newArray(int i) {
            return new Pelicula[i];
        }
    };
}
