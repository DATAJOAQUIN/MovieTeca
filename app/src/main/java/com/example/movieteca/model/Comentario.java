package com.example.movieteca.model;

import com.google.firebase.database.ServerValue;

public class Comentario {

    private String contenido, user_id,user_name;
    private Object timestamp;

    public Comentario() {
    }

    public Comentario(String contenido, String user_id, String user_name) {
        this.contenido = contenido;
        this.user_id = user_id;
        this.user_name = user_name;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
