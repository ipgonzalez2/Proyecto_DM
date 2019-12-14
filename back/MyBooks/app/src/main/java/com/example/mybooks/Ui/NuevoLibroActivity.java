package com.example.mybooks.Ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mybooks.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.*;

public class NuevoLibroActivity extends AppCompatActivity {
    final int REQUEST_CODE_GALLERY = 999;
    File file;
    String path;
    Bitmap bitmap;
    ImageButton BT_IMAGEN;
    boolean estaImagen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_libro);

        final Button BT_INSERTA = this.findViewById(R.id.btInsertaLibro);
        final Button BT_CANCELA = this.findViewById(R.id.btCancelaInsercion);
        BT_IMAGEN = this.findViewById(R.id.btImagen);
        estaImagen = false;

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

        BT_IMAGEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        NuevoLibroActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        Switch simpleSwitch = (Switch) findViewById(R.id.switchRead);
        simpleSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Buscamos los elementos necesarios
                Switch aSwitch = findViewById(R.id.switchRead);
                RatingBar ratingBar = findViewById(R.id.ratingBar);
                EditText description = findViewById(R.id.edReseña);
                //Comprobamos si el botón ha sido activado
                Boolean switchState = aSwitch.isChecked();

                if(switchState) {
                    description.setVisibility(View.VISIBLE);
                    ratingBar.setVisibility(View.VISIBLE);

                }else{
                    description.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri imagenSeleccionada = data.getData();
            String[] fillPath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagenSeleccionada, fillPath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(fillPath[0]));
            cursor.close();
            bitmap = BitmapFactory.decodeFile(path);
            BT_IMAGEN.setImageBitmap(bitmap);
            estaImagen = true;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void guarda(){

        final EditText ED_TITULO = this.findViewById( R.id.edTitulo );
        final EditText ED_AUTOR = this.findViewById( R.id.edAutor );
        final Switch LEIDO = this.findViewById(R.id.switchRead);
        final RatingBar PUNTUACION = this.findViewById(R.id.ratingBar);
        final EditText ED_RESEÑA = this.findViewById(R.id.edReseña);

        //final EditText ED_IMAGEN = this.findViewById( R.id.edImagen );

        final Spinner SP_GENERO = this.findViewById( R.id.spinnerGenero );
        final int GENERO = SP_GENERO.getSelectedItemPosition();
        final Intent DATOS = new Intent();

        DATOS.putExtra("titulo", ED_TITULO.getText().toString());
        DATOS.putExtra("autor", ED_AUTOR.getText().toString());

        DATOS.putExtra("genero", GENERO);
        DATOS.putExtra("leido", LEIDO.isChecked());
        if(estaImagen) {
            DATOS.putExtra("imagen", path);
            //SaveImage(bitmap);
        }else{
            DATOS.putExtra("imagen", "null");
        }
        if(LEIDO.isChecked()){
            DATOS.putExtra("puntuacion", PUNTUACION.getRating());
            DATOS.putExtra("reseña", ED_RESEÑA.getText().toString() );
        }else{
            DATOS.putExtra("puntuacion", 20);
            DATOS.putExtra("reseña", "");
        }

        this.setResult( Activity.RESULT_OK, DATOS );
        this.finish();
    }

}
