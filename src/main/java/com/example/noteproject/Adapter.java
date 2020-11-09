package com.example.noteproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Note> notes;

    Adapter(Context context, List<Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i) {
        String subject = notes.get(i).getSubject();
        viewHolder.nSubject.setText(subject);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nSubject = itemView.findViewById(R.id.nSubject);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),NoteBody.class);
                    i.putExtra("ID",notes.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
