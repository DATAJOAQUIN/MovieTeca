package com.example.movieteca;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.movieteca.model.Pelicula;
import com.squareup.picasso.Picasso;

public class PeliDetalleActivity extends AppCompatActivity {
    private final static String URL_FOTO = "https://image.tmdb.org/t/p/";
    private final static String TAMAÑO_LOGO = "w500";

    private TextView titulo,fecha, puntuacion, sinopsis;
    private ImageView poster;
    private RatingBar estrellas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        ActionBar actionBar=this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Pelicula pelicula=getIntent().getParcelableExtra("pelicula");

        titulo=findViewById(R.id.titulo);
        fecha=findViewById(R.id.releaseDate_tv);
        puntuacion=findViewById(R.id.voteAverage_tv);
        sinopsis=findViewById(R.id.overview_tv);
        poster=findViewById(R.id.moviePoster_iv);
        estrellas=findViewById(R.id.rating);

        if (pelicula!=null){
            titulo.setText(pelicula.getTitle());
            fecha.setText(pelicula.getReleaseDate());
            puntuacion.setText(pelicula.getVoteAverage());
            sinopsis.setText(pelicula.getOverview());
            estrellas.setRating(Float.parseFloat(pelicula.getVoteAverage()));
            cargarImagen(pelicula.getPosterPath());
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
