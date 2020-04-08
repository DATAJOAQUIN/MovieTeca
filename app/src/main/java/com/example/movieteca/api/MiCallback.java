package com.example.movieteca.api;

import com.example.movieteca.model.Pelicula;

public interface MiCallback {
    void updateAdapter(Pelicula[] movies);
}
