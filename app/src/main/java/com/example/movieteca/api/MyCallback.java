package com.example.movieteca.api;

import com.example.movieteca.model.Pelicula;

public interface MyCallback {
    void updateAdapter(Pelicula[] movies);
}
