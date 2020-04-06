package com.example.movieteca.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.movieteca.R;
import com.example.movieteca.adapter.PelisAdapter;
import com.example.movieteca.api.FetchMovieTask;
import com.example.movieteca.api.MyCallback;
import com.example.movieteca.model.Pelicula;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private static final String KEY_MOVIE = "MOVIE";
    private static final String KEY_MOVIE_LIST = "MOVIE_LIST";
    private GridView gridView;
    private ArrayList<Pelicula> movieList;
    private PelisAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        gridView= root.findViewById(R.id.movies_grid_view);
        adapter=new PelisAdapter(root.getContext(),movieList);
        gridView.setAdapter(adapter);

        cargarPelis();

        return root;
    }

    private void cargarPelis(){
        FetchMovieTask moviesTask = new FetchMovieTask(new MyCallback() {
            @Override
            public void updateAdapter(Pelicula[] movies) {

                if (movies != null) {
                    adapter.clear();
                    Collections.addAll(movieList, movies);
                    adapter.notifyDataSetChanged();
                }

            }

        });
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortingOrder = "popular";
        moviesTask.execute("top_rated");
    }
}
