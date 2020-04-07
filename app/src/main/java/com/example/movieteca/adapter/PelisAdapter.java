package com.example.movieteca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieteca.R;
import com.example.movieteca.model.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PelisAdapter extends BaseAdapter {



    private final static String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private final static String IMAGE_SIZE = "w500";
    private final Context context;
    private final List<Pelicula> movies;

    public PelisAdapter(Context context, List<Pelicula> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Pelicula getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pelicula movie = getItem(position);
        View cardView;
        ImageView imageView;
        TextView titulo;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cardView = inflater.inflate(R.layout.movie_card, parent, false);
        }else{
            cardView = convertView;}
            imageView = cardView.findViewById(R.id.poster_iv);
            titulo=cardView.findViewById(R.id.title_tv);
            titulo.setText(movie.getTitle());
            String url = new StringBuilder()
                    .append(BASE_POSTER_URL)
                    .append(IMAGE_SIZE)
                    .append(movie.getPosterPath().trim()).toString();

            Picasso.with(context)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
            return cardView;

    }

    public void clear() {
        if (movies.size() > 0) {
            movies.clear();
        }
    }
}
