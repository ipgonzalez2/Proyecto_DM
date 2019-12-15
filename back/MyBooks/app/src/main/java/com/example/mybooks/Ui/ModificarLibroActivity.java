package com.example.mybooks.Ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mybooks.R;

import java.io.File;

public class ModificarLibroActivity extends AppCompatActivity {
    final int REQUEST_CODE_GALLERY = 999;
    File file;
    String path;
    Bitmap bitmap;
    boolean estaImagen;
    ImageButton imagen;
    EditText ED_RESEÑA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_libro);
        final Intent datos = this.getIntent();
        imagen = this.findViewById(R.id.btImagen);
        TextView titulo = this.findViewById(R.id.edTitulo);
        TextView autor = this.findViewById(R.id.edAutor);
        TextView genero = this.findViewById(R.id.edTematica);
        Switch liLeido = this.findViewById(R.id.switchRead);
        EditText reseña = this.findViewById(R.id.edReseña);
        RatingBar puntuacion = this.findViewById(R.id.ratingBar);

        //ASIGNAMOS VALORES
        Bitmap bitmap = BitmapFactory.decodeFile(datos.getExtras().getString("imagen"));
        imagen.setImageBitmap(bitmap);
        titulo.setText(datos.getExtras().getString("titulo"));
        autor.setText(datos.getExtras().getString("autor"));
        genero.setText(datos.getExtras().getString("genero"));
        int leido = datos.getExtras().getInt("leido");


        if(leido == 1){
            liLeido.setChecked(true);
            reseña.setVisibility(View.VISIBLE);
            puntuacion.setVisibility(View.VISIBLE);
            reseña.setText(datos.getExtras().getString("reseña"));
            puntuacion.setRating(Float.parseFloat(datos.getExtras().getString("puntuacion")));
        }

        final Button modificar = this.findViewById(R.id.btModificaLibro);
        ED_RESEÑA = this.findViewById(R.id.edReseña);

        ED_RESEÑA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                modificar.setEnabled(ED_RESEÑA.getText().toString().trim().length()>0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                modificar.setEnabled(ED_RESEÑA.getText().toString().trim().length()>0);}
        });

        liLeido.setOnClickListener(new View.OnClickListener() {

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
                    modificar.setEnabled(ED_RESEÑA.getText().toString().trim().length()>0);

                }else{
                    description.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    modificar.setEnabled(true);
                }
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ModificarLibroActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificarLibroActivity.this.guarda();
            }
        });
        Button cancelar = this.findViewById(R.id.btCancelaModificar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificarLibroActivity.this.finish();
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
            imagen.setImageBitmap(bitmap);
            estaImagen = true;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void guarda(){
        final Intent datos = this.getIntent();
        final TextView ED_TITULO = this.findViewById( R.id.edTitulo );
        final TextView ED_AUTOR = this.findViewById( R.id.edAutor );
        final Switch LEIDO = this.findViewById(R.id.switchRead);
        final RatingBar PUNTUACION = this.findViewById(R.id.ratingBar);

        //final EditText ED_IMAGEN = this.findViewById( R.id.edImagen );

        final TextView GENERO = this.findViewById( R.id.edTematica );
        //final int GENERO = SP_GENERO.getSelectedItemPosition();
        final Intent DATOS = new Intent();

        DATOS.putExtra("titulo", ED_TITULO.getText().toString());
        DATOS.putExtra("autor", ED_AUTOR.getText().toString());


        DATOS.putExtra("genero", GENERO.getText().toString());
        DATOS.putExtra("leido", LEIDO.isChecked());
        DATOS.putExtra("id",datos.getExtras().getInt("id"));

        if(estaImagen) {
            DATOS.putExtra("imagen", path);
            //SaveImage(bitmap);
        }else{
            DATOS.putExtra("imagen",datos.getExtras().getString("imagen") );
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
