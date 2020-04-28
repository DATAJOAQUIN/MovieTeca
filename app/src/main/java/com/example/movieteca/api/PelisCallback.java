package com.example.movieteca.api;

import com.example.movieteca.model.Pelicula;

public interface PelisCallback {
    void updateAdapter(Pelicula[] movies);
}
