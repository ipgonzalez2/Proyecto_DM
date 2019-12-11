package com.example.bookkeep.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookkeep.R;
import com.example.bookkeep.Core.DBManager;
import com.example.bookkeep.Core.Libro;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static int RC_NUEVA_PRACTICA = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listeners
        final Button BT_INSERTA = this.findViewById( R.id.btInserta );
        final ListView LV_LIBROS = this.findViewById( R.id.lvLibros );

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.inserta();
            }
        });

        this.adaptador = new SimpleCursorAdapter(
                this,
                R.layout.entrada_libro,
                null,
                new String[]{ DBManager.CAMPO_TITULO, DBManager.CAMPO_AUTOR },
                new int[]{ R.id.lblTitulo, R.id.lblAutor }
        );

        LV_LIBROS.setAdapter( this.adaptador );

        this.registerForContextMenu( LV_LIBROS );

        // Actualiza la vista
        this.muestraEstado();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        this.dbManager = new DBManager( this.getApplicationContext() );

        this.adaptador.changeCursor( this.dbManager.getAllCursor() );

        this.muestraEstado();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate( R.menu.menu_ppal, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.opInserta:
                this.inserta();
                break;
        }

        return toret;
    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if ( v.getId() == R.id.lvLibros ) {
            this.getMenuInflater().inflate( R.menu.menu_ctx, menu );
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.opBorra:
                int pos = ( (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo() ).position;
                this.borraNum( pos );
                break;
        }

        return toret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if ( requestCode == RC_NUEVA_PRACTICA
                && resultCode == Activity.RESULT_OK )
        {
            System.out.println(this.dbManager.getAllCursor());
            final Libro LIBRO_NUEVO =
                    new Libro(
                            data.getExtras().getString( "titulo" ),
                            data.getExtras().getString( "autor" ),
                            data.getExtras().getString( "imagen" ),
                            Libro.Genero.ROMANTICA,
                            data.getExtras().getBoolean( "leido" ),
                            data.getExtras().getString( "reseña" ),
                            data.getExtras().getDouble( "puntuacion" ));

            /*
            // Ya no es necesario guardar en preferencias
            final SharedPreferences PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE );
            final SharedPreferences.Editor EDIT_PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE ).edit();
            final Set<String> PRACTICAS = PREFS.getStringSet( "practicas", new HashSet<String>() );

            PRACTICAS.add( PRACTICA_NUEVA.toString() );
            EDIT_PREFS.putStringSet( "practicas", PRACTICAS );
            EDIT_PREFS.apply();
            */

            this.dbManager.guarda( LIBRO_NUEVO );
        }

        return;
    }

    /*
    // Esta fn. era inserta() cuando para insertar un nuevo trabajo se utilizaba un dlg.
    private void insertaConDialogo()
    {
        AlertDialog.Builder dlg = new AlertDialog.Builder( this );
        final EditText ED_PRACTICA = new EditText( this );

        dlg.setTitle( "Práctica (asignatura: práctica)" );
        dlg.setView( ED_PRACTICA );
        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] partes = ED_PRACTICA.getText().toString().split( ":" );

                if (partes.length > 1 ) {
                    MainActivity.this.adaptador.add(
                            new Practica( partes[ 0 ], partes[ 1 ] )
                    );
                } else {
                    Toast.makeText( MainActivity.this,
                            "Error: siga el formato:\n <asignatura>: <trabajo>",
                                 Toast.LENGTH_LONG ).show();
                }

                MainActivity.this.muestraEstado();
            }
        });

        dlg.show();
    }*/

    private void inserta()
    {
        this.startActivityForResult( new Intent( this, NuevoLibroActivity.class ), RC_NUEVA_PRACTICA );
    }

    private void borraNum(int pos)
    {
        // this.practicas.remove( pos );
        // this.adaptador.notifyDataSetChanged();
        final Cursor CURSOR = this.adaptador.getCursor();

        CURSOR.moveToPosition( pos );
        int id = CURSOR.getInt( CURSOR.getColumnIndexOrThrow( dbManager.CAMPO_ID ) );
        this.dbManager.borra( id );
        this.adaptador.changeCursor( this.dbManager.getAllCursor() );

        this.muestraEstado();
    }

    private void muestraEstado()
    {
        final TextView LBL_PRACTICAS = this.findViewById( R.id.lblLibros );

        LBL_PRACTICAS.setText(
                Integer.toString( this.adaptador.getCount() )
                        + " tarea(s)."
        );
    }

    private SimpleCursorAdapter adaptador;
    //private ArrayList<Practica> practicas;
    private DBManager dbManager;
}
