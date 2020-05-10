package com.example.movieteca;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.adapter.TrailersAdapter;
import com.example.movieteca.api.BuscaTrailerTask;
import com.example.movieteca.api.TrailerCallback;
import com.example.movieteca.database.FavoriteDbHelper;
import com.example.movieteca.model.Pelicula;
import com.example.movieteca.model.Trailer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeliDetalleActivity extends AppCompatActivity {
    private final static String URL_FOTO = "https://image.tmdb.org/t/p/";
    private final static String TAMAÑO_LOGO = "w500";

    private TextView titulo,fecha, puntuacion, sinopsis;
    private ImageView poster;
    private RatingBar estrellas;
    private RecyclerView trailersView;
    private List<Trailer> listaTrailer;
    private TrailersAdapter adapterTrailer;
    private FloatingActionButton favoriteBoton;

    private  Pelicula pelicula;
    private int peliId;
    private boolean esFavorito;
    private FavoriteDbHelper dbAuxiliar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        ActionBar actionBar=this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pelicula=getIntent().getParcelableExtra("pelicula");

        titulo=findViewById(R.id.titulo);
        fecha=findViewById(R.id.releaseDate_tv);
        puntuacion=findViewById(R.id.voteAverage_tv);
        sinopsis=findViewById(R.id.overview_tv);
        poster=findViewById(R.id.moviePoster_iv);
        estrellas=findViewById(R.id.rating);

        favoriteBoton=findViewById(R.id.favoriteFab_id);
        favoriteBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcaFavorito();
            }
        });

        if (pelicula!=null){
            titulo.setText(pelicula.getTitle());
            fecha.setText(pelicula.getReleaseDate());
            puntuacion.setText(pelicula.getVoteAverage());
            sinopsis.setText(pelicula.getOverview());
            estrellas.setRating(Float.parseFloat(pelicula.getVoteAverage()));
            cargarImagen(pelicula.getPosterPath());
        }

        listaTrailer=new ArrayList<>();
        adapterTrailer=new TrailersAdapter(this,listaTrailer);

        trailersView=findViewById(R.id.trailersRecycleView);
        LinearLayoutManager trailersLayout=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        trailersView.setLayoutManager(trailersLayout);
        trailersView.setAdapter(adapterTrailer);
        adapterTrailer.notifyDataSetChanged();

        peliId=pelicula.getMovieId();

        comprobarFavorito();
        cargarTrailers(peliId);

    }

    private void cargarImagen(String ruta){
        String urlBuilder = new StringBuilder()
                .append(URL_FOTO)
                .append(TAMAÑO_LOGO)
                .append(ruta).toString();

        Picasso.with(this)
                .load(urlBuilder)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .into(poster);
    }

    public void cargarTrailers(int peliId){
        final BuscaTrailerTask trailerTask = new BuscaTrailerTask(new TrailerCallback() {
            @Override
            public void updateAdapter(Trailer[] trailers) {
                if (listaTrailer!=null){
                    adapterTrailer.clear();
                    Collections.addAll(listaTrailer,trailers);
                    adapterTrailer.notifyDataSetChanged();
                }
            }

        });

        trailerTask.execute(peliId);
    }

    public void comprobarFavorito(){
        dbAuxiliar= new FavoriteDbHelper(this);

        if (dbAuxiliar.searchFavorite(peliId)){
            favoriteBoton.setImageResource(R.drawable.redheart);
            esFavorito=true;
        }else {
            favoriteBoton.setImageResource(R.drawable.whiteheart);
            esFavorito=false;
        }
    }

    public void marcaFavorito(){
        if (esFavorito){
            favoriteBoton.setImageResource(R.drawable.whiteheart);
            Toast.makeText(this,"Eliminada de favoritos",Toast.LENGTH_LONG).show();
            dbAuxiliar.deleteFavorite(peliId);

            esFavorito=false;
        }else {
            favoriteBoton.setImageResource(R.drawable.redheart);
            Toast.makeText(this,"Añadida a favoritos",Toast.LENGTH_LONG).show();
            //dbAuxiliar.addFavorite(pelicula);
            dbAuxiliar.addFavorite(pelicula);
            esFavorito=true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
