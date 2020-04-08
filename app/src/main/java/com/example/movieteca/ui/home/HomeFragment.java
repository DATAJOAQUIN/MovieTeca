package com.example.movieteca.ui.home;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.R;
import com.example.movieteca.adapter.PelisAdapter;
import com.example.movieteca.api.BuscaPelisTask;
import com.example.movieteca.api.MiCallback;
import com.example.movieteca.model.Pelicula;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private static final String KEY_MOVIE = "MOVIE";
    private static final String KEY_MOVIE_LIST = "MOVIE_LIST";
    private RecyclerView recyclerView;
    private ArrayList<Pelicula> movieList;
    private PelisAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView= root.findViewById(R.id.recyler_view);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        }
        adapter=new PelisAdapter(root.getContext(),movieList);
        recyclerView.setAdapter(adapter);

        cargarPelis();

        return root;
    }

    private void cargarPelis(){
        BuscaPelisTask moviesTask = new BuscaPelisTask(new MiCallback() {
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
        moviesTask.execute("popular");
    }
}
