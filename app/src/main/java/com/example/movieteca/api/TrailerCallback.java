package com.example.movieteca.api;

import com.example.movieteca.model.Trailer;

public interface TrailerCallback {
    void updateAdapter(Trailer[] trailers);
}
