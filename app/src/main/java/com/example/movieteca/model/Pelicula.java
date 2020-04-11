package com.example.movieteca.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pelicula implements Parcelable {


    private final String title;
    private final String posterPath;
    private final String overview;
    private final String voteAverage;
    private final String releaseDate;

    public Pelicula(String title, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    private Pelicula(Parcel parcel) {
        title = parcel.readString();
        posterPath = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readString();
        releaseDate = parcel.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Pelicula> CREATOR = new Parcelable.Creator<Pelicula>() {

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
