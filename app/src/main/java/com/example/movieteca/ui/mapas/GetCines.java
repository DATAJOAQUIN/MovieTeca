package com.example.movieteca.ui.mapas;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.movieteca.api.NetworkRequest;

import org.json.JSONException;

;


public class GetCines extends AsyncTask<String,Void,String> {

    private final CinesCallBack cinesTaskllamada;

    public GetCines(CinesCallBack cinesTaskllamada) {
        this.cinesTaskllamada = cinesTaskllamada;
    }

    @Override
    protected String doInBackground(String... lugaresURL) {

        Uri uri=Uri.parse(lugaresURL[0]).buildUpon().build();

        String jsonString= NetworkRequest.getJsonString(uri);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String respuesta) {
        try {

            cinesTaskllamada.actualizar_marcadores(respuesta);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
