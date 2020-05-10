package com.example.movieteca.ui.buscar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.movieteca.model.Pelicula;

import java.util.ArrayList;
import java.util.Collections;

public class BuscarFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<Pelicula> movieList;
    private PelisAdapter adapter;
    private Button buscaBoton;
    private EditText busquedaView;
    private TextView textoResultado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buscar, container, false);
        busquedaView = root.findViewById(R.id.texto_busqueda);
        textoResultado=root.findViewById(R.id.texto_resultado);


        recyclerView=root.findViewById(R.id.recyler_view);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 4));
        }

        movieList=new ArrayList<>();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new PelisAdapter(root.getContext(),movieList);
        recyclerView.setAdapter(adapter);

        buscaBoton=root.findViewById(R.id.busca_boton);

        buscaBoton.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        String buscar= busquedaView.getText().toString();

        buscarPeliculas(buscar);

        Log.d("Texto a buscar",buscar);
    }

    public void buscarPeliculas(String query){
        BuscaPelisTask moviesTask = new BuscaPelisTask(new PelisCallback() {
            @Override
            public void updateAdapter(Pelicula[] movies) {

                if (movies != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textoResultado.setVisibility(View.INVISIBLE);
                    adapter.clear();
                    Collections.addAll(movieList, movies);
                    adapter.notifyDataSetChanged();
                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    textoResultado.setVisibility(View.VISIBLE);
                    textoResultado.setText("No hay resultados");
                }
            }

        });

        moviesTask.execute(query);

    }
}
