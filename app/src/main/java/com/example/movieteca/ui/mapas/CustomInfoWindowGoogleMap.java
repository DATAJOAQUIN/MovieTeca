package com.example.movieteca.ui.mapas;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movieteca.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private final View view;
    private final static String BASE_IMG_URL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=200";
    private final static String API_KEY="AIzaSyA-aKcIRzRzYbnBamLDVK2h9xPoUymqR_A";

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
        view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_window_info, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView nombre = view.findViewById(R.id.nombre);
        TextView direccion= view.findViewById(R.id.direccion);
        TextView valoracion=view.findViewById(R.id.val_numero);
        RatingBar estrellas=view.findViewById(R.id.val_bar);
        ImageView img = view.findViewById(R.id.pic);

        nombre.setText(marker.getTitle());

        CineWindow cineWindow = (CineWindow) marker.getTag();

        String url = new StringBuilder()
                    .append(BASE_IMG_URL)
                    .append("&photoreference="+ cineWindow.getImagen())
                    .append("&key="+API_KEY).toString();
        Log.d("FALLO",url);


        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.cine_sala)
                .error(R.mipmap.ic_launcher)
                .into(img);

        direccion.setText(cineWindow.getDireccion());
        valoracion.setText(String.valueOf(cineWindow.getValoracion()));
        estrellas.setRating(Float.parseFloat(cineWindow.getValoracion().toString()));

        return view;
    }


}
