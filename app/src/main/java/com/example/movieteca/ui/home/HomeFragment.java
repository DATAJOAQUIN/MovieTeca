package com.example.movieteca.ui.home;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.R;
import com.example.movieteca.adapter.PelisAdapter;
import com.example.movieteca.api.BuscaPelisTask;
import com.example.movieteca.api.PelisCallback;
import com.example.movieteca.database.FavoriteDbHelper;
import com.example.movieteca.model.Pelicula;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private static final String KEY_MOVIE_LIST = "MOVIE_LIST";
    private RecyclerView recyclerView;
    private ArrayList<Pelicula> movieList;
    private PelisAdapter adapter;
    private FavoriteDbHelper favoriteDbHelper;

    private ImageView noWifiImage;
    private TextView messageText;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        noWifiImage=root.findViewById(R.id.nowifi_iv);
        messageText=root.findViewById(R.id.nowifi_tv);

        recyclerView= root.findViewById(R.id.recyler_view);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new PelisAdapter(root.getContext(),movieList);
        recyclerView.setAdapter(adapter);

        favoriteDbHelper=new FavoriteDbHelper(getActivity());

        comprobarOrden();

        return root;
    }

    public void comprobarOrden(){
        SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortingOrder = preferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popular_value));

        if (!sortingOrder.equals(getString(R.string.pref_sort_favorites_value))){
            cargarNoFavoritos(sortingOrder);
        }else {
            cargarFavoritos();
        }

    }


    public void cargarNoFavoritos(String sortingOrder){
        BuscaPelisTask moviesTask = new BuscaPelisTask(new PelisCallback() {
            @Override
            public void updateAdapter(Pelicula[] movies) {

                if (movies != null) {
                    adapter.clear();
                    Collections.addAll(movieList, movies);
                    adapter.notifyDataSetChanged();
                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noWifiImage.setVisibility(View.VISIBLE);
                    messageText.setVisibility(View.VISIBLE);
                }
            }

        });

        moviesTask.execute(sortingOrder);
    }

    public void cargarFavoritos(){
       new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieList.clear();
                movieList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();

                if (movieList.isEmpty()){
                    recyclerView.setVisibility(View.INVISIBLE);
                    noWifiImage.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageText.setText("Lista de favoritos vacía");
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noWifiImage.setVisibility(View.GONE);
                    messageText.setVisibility(View.INVISIBLE);
                }
            }
        }.execute();


    }

    @Override
    public void onStart() {
        super.onStart();
        comprobarOrden();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIE_LIST, movieList);
        super.onSaveInstanceState(outState);
    }

}


