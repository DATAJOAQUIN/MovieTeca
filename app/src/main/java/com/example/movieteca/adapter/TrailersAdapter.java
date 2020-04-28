package com.example.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.R;
import com.example.movieteca.model.Trailer;

import java.util.List;

public class TrailersAdapter  extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private static final String YOUTUBE_URL_BROWSER = "https://www.youtube.com/watch";

    private Context contexto;
    private List<Trailer> listaTrailers;

    public TrailersAdapter(Context contexto, List<Trailer> listaTrailers) {
        this.contexto = contexto;
        this.listaTrailers = listaTrailers;
    }

    @NonNull
    @Override
    public TrailersAdapter.TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_card, viewGroup, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder viewHolder, int position) {
        viewHolder.titulo.setText(listaTrailers.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return listaTrailers.size();
    }

    public void clear() {
        if (listaTrailers.size() > 0) {
            listaTrailers.clear();
        }
    }

    public class TrailerHolder extends RecyclerView.ViewHolder{
        public TextView titulo;
        public ImageView thumbnail;

        public TrailerHolder(@NonNull View view) {
            super(view);
            titulo=view.findViewById(R.id.titulo_video);
            thumbnail=view.findViewById(R.id.imageYoutube);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        Trailer itemClicado=listaTrailers.get(pos);
                        String trailerKey=itemClicado.getKey();

                        Uri uri = Uri.parse(YOUTUBE_URL_BROWSER)
                                .buildUpon()
                                .appendQueryParameter("v", trailerKey)
                                .build();

                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        contexto.startActivity(intent);
                        //Toast.makeText(v.getContext(), "Has clicado " + itemClicado.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
