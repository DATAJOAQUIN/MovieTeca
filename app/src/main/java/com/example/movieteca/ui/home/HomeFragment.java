package com.example.movieteca.ui.home;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView recyclerView;
    private ArrayList<Pelicula> movieList;
    private PelisAdapter adapter;

    private ImageView noWifiImage;
    private TextView noWifiText;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        noWifiImage=root.findViewById(R.id.nowifi_iv);
        noWifiText=root.findViewById(R.id.nowifi_tv);

        recyclerView= root.findViewById(R.id.recyler_view);


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 4));
        }


        adapter=new PelisAdapter(root.getContext(),movieList);
        recyclerView.setAdapter(adapter);


        cargarPelis();

        return root;
    }

    public void cargarPelis(){
        FetchMovieTask moviesTask = new FetchMovieTask(new MyCallback() {
            @Override
            public void updateAdapter(Pelicula[] movies) {

                if (movies != null) {
                    adapter.clear();
                    Collections.addAll(movieList, movies);
                    adapter.notifyDataSetChanged();
                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noWifiImage.setVisibility(View.VISIBLE);
                    noWifiText.setVisibility(View.VISIBLE);
                }

            }

        });
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortingOrder = preferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popular_value));
        moviesTask.execute(sortingOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarPelis();
    }
}


