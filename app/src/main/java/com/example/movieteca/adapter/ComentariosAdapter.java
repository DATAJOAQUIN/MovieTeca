package com.example.movieteca.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieteca.R;
import com.example.movieteca.model.Comentario;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentarioViewHolder> {

    private Context contexto;
    private List<Comentario> commentList;

    public ComentariosAdapter(Context contexto, List<Comentario> commentList) {
        this.contexto = contexto;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(contexto).inflate(R.layout.comment_item,parent,false);
        return new ComentarioViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {

        holder.user_name.setText(commentList.get(position).getUser_name());
        holder.contenido.setText(commentList.get(position).getContenido());
        holder.fecha.setText(timestampToString((Long)commentList.get(position).getTimestamp()));

        Picasso.with(contexto)
                .load(commentList.get(position).getUser_img())
                .placeholder(R.drawable.user_default)
                .error(R.mipmap.ic_launcher)
                .into(holder.user_img);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class ComentarioViewHolder extends RecyclerView.ViewHolder{

        ImageView user_img;
        TextView user_name, contenido, fecha;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            user_img=itemView.findViewById(R.id.comment_user_img);
            user_name=itemView.findViewById(R.id.comment_username);
            contenido=itemView.findViewById(R.id.comment_content);
            fecha=itemView.findViewById(R.id.comment_date);
        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yy",calendar).toString();
        return date;

    }
}
