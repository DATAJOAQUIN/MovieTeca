package com.example.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.PeliDetalleActivity;
import com.example.movieteca.R;
import com.example.movieteca.model.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PelisAdapter extends RecyclerView.Adapter<PelisAdapter.PelisHolder> {

    private final static String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private final static String IMAGE_SIZE = "w500";
    private final Context context;
    private final List<Pelicula> movies;
    private String menu;

    public PelisAdapter(Context context, List<Pelicula> movies) {
        this.context = context;
        this.movies = movies;

    }

    @NonNull
    @Override
    public PelisHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new PelisHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PelisHolder viewHolder, int i) {
        viewHolder.titulo.setText(movies.get(i).getTitle());
        String vote = movies.get(i).getVoteAverage();
        viewHolder.puntuacion.setText(vote);

        ImageView imageView=viewHolder.poster;

        String url = new StringBuilder()
                .append(BASE_POSTER_URL)
                .append(IMAGE_SIZE)
                .append(movies.get(i).getPosterPath().trim()).toString();

        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .into(imageView);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void clear() {
        if (movies.size() > 0) {
            movies.clear();
        }
    }

    public class PelisHolder extends RecyclerView.ViewHolder{
        public TextView titulo, puntuacion;
        public ImageView poster;

        public PelisHolder(@NonNull View view) {
            super(view);
            titulo = (TextView) view.findViewById(R.id.title_tv);
            puntuacion = (TextView) view.findViewById(R.id.rating_tv);
            poster = (ImageView) view.findViewById(R.id.poster_iv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        Pelicula itemClicado=movies.get(pos);
                        Intent intent=new Intent(context, PeliDetalleActivity.class);
                        intent.putExtra("pelicula",itemClicado);

                        context.startActivity(intent);
                        //Toast.makeText(v.getContext(), "Has clicado " + itemClicado.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
