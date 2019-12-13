package com.example.mybooks.Ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybooks.R;

public class VerLibroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_libro);
        Button atras = this.findViewById(R.id.btVerAtras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerLibroActivity.this.finish();
            }
        });
        final Intent datos = this.getIntent();
        //RECUPERAMOS ELEMENTOS VISTA
        ImageView imagen = this.findViewById(R.id.btImagen);
        TextView titulo = this.findViewById(R.id.edTitulo);
        TextView autor = this.findViewById(R.id.edAutor);
        TextView genero = this.findViewById(R.id.edTematica);
        TextView reseña = this.findViewById(R.id.edReseña);
        RatingBar puntuacion = this.findViewById(R.id.ratingBar);

        //ASIGNAMOS VALORES
        Bitmap bitmap = BitmapFactory.decodeFile(datos.getExtras().getString("imagen"));
        imagen.setImageBitmap(bitmap);
        titulo.setText(datos.getExtras().getString("titulo"));
        autor.setText(datos.getExtras().getString("autor"));
        genero.setText(datos.getExtras().getString("genero"));
        int leido = datos.getExtras().getInt("leido");
        if(leido == 1){
            reseña.setVisibility(View.VISIBLE);
            puntuacion.setVisibility(View.VISIBLE);
            reseña.setText(datos.getExtras().getString("reseña"));
            puntuacion.setRating(Float.parseFloat(datos.getExtras().getString("puntuacion")));
            puntuacion.setEnabled(false);
        }



    }
}
