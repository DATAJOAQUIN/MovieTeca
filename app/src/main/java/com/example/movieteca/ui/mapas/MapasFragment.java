package com.example.movieteca.ui.mapas;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.movieteca.R;
import com.example.movieteca.api.CinesCallBack;
import com.example.movieteca.api.GetCines;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapasFragment extends Fragment implements OnMapReadyCallback {
    final int PETICION_PERMISO_COARSE = 0;
    final int PETICION_PERMISO_FINE =1;
    private FusedLocationProviderClient fusedLocationClient;
    private int userIcon, cineIcon;

    private SupportMapFragment mapFragment;
    private View view;
    private Location location;
    private MarkerOptions[] cines;
    private Marker[] marcaCines;
    private final int MAX_CINES=20;

    double mLatitude = 0;
    double mLongitude = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mapas, container, false);
        view=root;

        //getMapAsync(this);
        // Getting Google Play availability status
        userIcon=R.drawable.user;
        cineIcon=R.drawable.cinema;

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();

        } else {
            // Getting reference to the SupportMapFragment
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
           pedirPermisoCoarse(root.getContext(),mapFragment);
        }



        return root;
    }
    public void pedirPermisoCoarse(Context context, SupportMapFragment mapFragment){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PETICION_PERMISO_COARSE);
        }else{
            pedirPermisoFine(context,mapFragment);
        }
    }
    public void pedirPermisoFine(Context context, SupportMapFragment mapFragment){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_FINE);
        }
        else{

            setGeoLocation();
        }
    }
    public void setGeoLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            setLocation(location);

                            prepararMapa();
                        }
                    }
                });
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void prepararMapa(){
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PETICION_PERMISO_COARSE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    pedirPermisoFine(view.getContext(),mapFragment);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case PETICION_PERMISO_FINE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setGeoLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
      mLatitude= location.getLatitude();
      mLongitude = location.getLongitude();


        // Posicionar el mapa en una localización y con un nivel de zoom
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        //map.setMaxZoomPreference(15);
        map.setMinZoomPreference(5);

        float zoom = 13;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // Colocar un marcador en la misma posición
        map.addMarker(new MarkerOptions()
                        .position(latLng).title("Estás aqui")
                        .icon(BitmapDescriptorFactory.fromResource(userIcon))
                        .snippet("Tú última ubicación"));

        // Más opciones para el marcador en:
        // https://developers.google.com/maps/documentation/android/marker

        map.animateCamera(CameraUpdateFactory.newLatLng(latLng),3000,null);

        actualizarLugares(map);

    }

    public void actualizarLugares(final GoogleMap map){
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                "json?location="+mLatitude+","+mLongitude+
                "&radius=500&sensor=true" +
                "&query=cines"+
                "&key=AIzaSyA-aKcIRzRzYbnBamLDVK2h9xPoUymqR_A";

       GetCines buscaCines=new GetCines(new CinesCallBack() {
           @Override
           public void actualizar_marcadores(String respuesta) {

               marcaCines=new Marker[MAX_CINES];

               Log.d("respuesta",respuesta);
               if (respuesta!=null){
                    try {
                        JSONObject resultObject=new JSONObject(respuesta);
                        JSONArray arrayLugares=resultObject.getJSONArray("results");

                        cines=new MarkerOptions[arrayLugares.length()];

                        //Log.d("tamanoArray", String.valueOf(arrayLugares.length()));
                        for (int i=0;i<arrayLugares.length();i++){
                            //parsear cada lugar
                            boolean missingValue=false;

                            LatLng lugarLL=null;
                            String nombreLugar="";
                            String direccion="";
                            String img_reference="";
                            Double valoracion=0.0;


                            try{
                                missingValue=false;
                                //intento de buscar  valores de datos del cine
                                JSONObject placeObject = arrayLugares.getJSONObject(i);
                                JSONObject loc = placeObject.getJSONObject("geometry").getJSONObject("location");
                               lugarLL = new LatLng(
                                        Double.valueOf(loc.getString("lat")),
                                        Double.valueOf(loc.getString("lng")));

                                direccion = placeObject.getString("formatted_address");
                                nombreLugar=placeObject.getString("name");
                                valoracion=placeObject.getDouble("rating");

                                img_reference = placeObject.getJSONArray("photos").getJSONObject(0).getString("photo_reference");

                               // Log.d("photos",img_reference);

                            }
                            catch(JSONException jse){
                                missingValue=true;
                                jse.printStackTrace();
                            }

                            if (missingValue){
                                cines[i]=null;
                            }else {
                                cines[i]=new MarkerOptions()
                                        .position(lugarLL)
                                        .title(nombreLugar)
                                        .icon(BitmapDescriptorFactory.fromResource(cineIcon));
                                        //.snippet(direccion);

                                CineWindow info=new CineWindow();
                                info.setImagen("user");
                                info.setDireccion(direccion);
                                info.setImagen(img_reference);
                                info.setValoracion(valoracion);

                                CustomInfoWindowGoogleMap customWindow=new CustomInfoWindowGoogleMap(getContext());
                                map.setInfoWindowAdapter(customWindow);

                                marcaCines[i]=map.addMarker(cines[i]);
                                marcaCines[i].setTag(info);
                                marcaCines[i].showInfoWindow();
                            }
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
               }
           }
       });

       buscaCines.execute(placesSearchStr);
    }

}
