package com.example.movieteca.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PelisRespuesta implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Pelicula> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Pelicula> getResults() {
        return results;
    }

    public List<Pelicula> getMovies() {
        return results;
    }

    public void setResults(List<Pelicula> results) {
        this.results = results;
    }

    public void setMovies(List<Pelicula> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.totalResults);
        dest.writeInt(this.totalPages);
    }

    public PelisRespuesta() {
    }

    protected PelisRespuesta(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Pelicula.CREATOR);
        this.totalResults = in.readInt();
        this.totalPages = in.readInt();
    }

    public static final Parcelable.Creator<PelisRespuesta> CREATOR = new Parcelable.Creator<PelisRespuesta>() {
        @Override
        public PelisRespuesta createFromParcel(Parcel source) {
            return new PelisRespuesta(source);
        }

        @Override
        public PelisRespuesta[] newArray(int size) {
            return new PelisRespuesta[size];
        }
    };
}
