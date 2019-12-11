package com.example.bookkeep.Ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookkeep.R;

public class NuevoLibroActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_libro);

        final Button BT_INSERTA = this.findViewById(R.id.btInserta);
        final Button BT_CANCELA = this.findViewById(R.id.btCancela);

        BT_CANCELA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoLibroActivity.this.finish();
            }
        });

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoLibroActivity.this.guarda();
            }
        });
    }

    private void guarda(){

        final EditText ED_TITULO = this.findViewById( R.id.edTitulo );
        final EditText ED_AUTOR = this.findViewById( R.id.edAutor );
        final EditText ED_IMAGEN = this.findViewById( R.id.edImagen );
        final EditText ED_GENERO = this.findViewById( R.id.edGenero );
        final Intent DATOS = new Intent();

        DATOS.putExtra("titulo", ED_TITULO.getText().toString());
        DATOS.putExtra("autor", ED_AUTOR.getText().toString());
        DATOS.putExtra("imagen", ED_IMAGEN.getText().toString());
        DATOS.putExtra("genero", ED_GENERO.getText().toString());
        DATOS.putExtra("leido", true);
        DATOS.putExtra("puntuacion", 0.0);
        DATOS.putExtra("reseña", "bien");



        this.setResult( Activity.RESULT_OK, DATOS );
        this.finish();
    }

}
