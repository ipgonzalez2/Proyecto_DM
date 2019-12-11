package com.example.bookkeep.Ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookkeep.Core.Libro;
import com.example.bookkeep.R;

import java.util.ArrayList;

public class LibroArrayAdapter extends ArrayAdapter<Libro> {

    public LibroArrayAdapter(Context context, ArrayList<Libro> lista)
    {
        super(context, 0, lista);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Libro LIBRO = this.getItem(position);

        if(convertView == null){
            final LayoutInflater INFLATER = LayoutInflater.from(this.getContext());
            convertView = INFLATER.inflate(R.layout.entrada_libro, null);
        }

        final TextView LBL_TITULO = convertView.findViewById(R.id.lblTitulo);
        final TextView LBL_AUTOR = convertView.findViewById(R.id.lblAutor);

        LBL_TITULO.setText( LIBRO.getTitulo());
        LBL_AUTOR.setText( LIBRO.getAutor() );

        return convertView;
    }
}

