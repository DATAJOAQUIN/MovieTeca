package com.example.movieteca;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.adapter.ComentariosAdapter;
import com.example.movieteca.adapter.TrailersAdapter;
import com.example.movieteca.api.BuscaTrailerTask;
import com.example.movieteca.api.TrailerCallback;
import com.example.movieteca.database.FavoriteDbHelper;
import com.example.movieteca.model.Comentario;
import com.example.movieteca.model.Pelicula;
import com.example.movieteca.model.Trailer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private Button addComment;
    private EditText editTextComment;
    private RecyclerView commentsView;
    private List<Comentario> commentList;
    private ComentariosAdapter commentAdapter;
    LinearLayout addCommentLayout;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;


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

        editTextComment=findViewById(R.id.peli_detail_comment);
        addComment=findViewById(R.id.add_comment_btn);
        commentsView=findViewById(R.id.comments_rv);
        addCommentLayout=findViewById(R.id.layout_add_comment);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contenido=editTextComment.getText().toString();
                String user_id=firebaseUser.getUid();
                String user_name=firebaseUser.getDisplayName();

                Comentario comentario=new Comentario(contenido,user_id,user_name);

                DatabaseReference commentReference=firebaseDatabase.getReference("comentarios").child(String.valueOf(peliId)).push();


                    commentReference.setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mostrarMensaje("Añadido correctamente");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mostrarMensaje("Fallo al añadir comentario: "+e.getMessage());
                        }
                    });

            }
        });

        cargarComentarios();

    }

    private void cargarComentarios(){
        commentsView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef=firebaseDatabase.getReference("comentarios").child(String.valueOf(peliId));
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList=new ArrayList<>();

                for (DataSnapshot snap:dataSnapshot.getChildren()){

                    Comentario comentario=snap.getValue(Comentario.class);
                    commentList.add(comentario);

                    if (comentario.getUser_id().equals(firebaseUser.getUid())){
                        //addCommentLayout.setVisibility(View.GONE);
                    }
                }

                commentAdapter=new ComentariosAdapter(getApplicationContext(),commentList);
                commentsView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
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
