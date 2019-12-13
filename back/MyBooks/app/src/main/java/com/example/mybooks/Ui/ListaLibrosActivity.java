package com.example.mybooks.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybooks.Core.DBManager;
import com.example.mybooks.Core.Libro;
import com.example.mybooks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.sql.SQLOutput;
import java.util.List;

public class ListaLibrosActivity extends AppCompatActivity {
    private static String LOG_TAG = ListaLibrosActivity.class.getSimpleName();
    private static int RC_NUEVO_LIBRO = 21;
    private static int RC_MODIFICAR_LIBRO = 11;
    private static int RC_VER_LIBRO = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_libros);
       /* ImageView img = this.findViewById(R.id.booooook);
        String path = "/storage/emulated/0/Download/el-libro-de-las-abejas.jpg";
        File file = new File(path);
        Bitmap bm = BitmapFactory.decodeFile(file.getPath());
        img.setImageBitmap(bm);*/
        // Listeners
        //final Button BT_INSERTA = this.findViewById( R.id.btInserta );
        final ListView LV_LIBROS = this.findViewById( R.id.lvLibros );

        /*BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.inserta();
            }
        });*/

        this.adaptador = new SimpleCursorAdapter(
                this,
                R.layout.entrada_libro,
                null,
                new String[]{ DBManager.CAMPO_TITULO, DBManager.CAMPO_AUTOR,DBManager.CAMPO_IMAGEN },
                new int[]{ R.id.lblTitulo, R.id.lblAutor, R.id.bookImage }
        );

        FloatingActionButton añadirLibro = this.findViewById(R.id.añadirLibro);

        añadirLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaLibrosActivity.this.inserta();
            }
        });

        LV_LIBROS.setAdapter( this.adaptador );

        LV_LIBROS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaLibrosActivity.this.ver(position);
                //ListaLibrosActivity.this.startActivityForResult( new Intent( ListaLibrosActivity.this, VerLibroActivity.class ), RC_VER_LIBRO );
            }
        });

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

    @Override
    public void onPause()
    {
        super.onPause();
        this.dbManager.close();
        this.adaptador.getCursor().close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate( R.menu.menu_opt, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.opSalir:
                this.finish();
                break;
        }

        return toret;
    }

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
                final int pos = ( (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo() ).position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaLibrosActivity.this);
                builder.setMessage("¿Desea borrar este libro?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListaLibrosActivity.this.borraNum(pos);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
                toret = true;
                break;
            case R.id.opModifica:
                this.startActivityForResult( new Intent( this, ModificarLibroActivity.class ), RC_MODIFICAR_LIBRO );
                break;

        }

        return toret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if ( requestCode == RC_NUEVO_LIBRO
                && resultCode == Activity.RESULT_OK )
        {
            System.out.println(this.dbManager.getAllCursor());
            System.out.println(data.getExtras().getString("imagen"));
            Libro.Genero genero = Libro.Genero.OTRO;

            if(data.getExtras().getInt("genero") > 0){
                genero = Libro.Genero.values()[data.getExtras().getInt("genero")];
            }


            final Libro LIBRO_NUEVO =
                    new Libro(
                            data.getExtras().getString( "titulo" ),
                            data.getExtras().getString( "autor" ),
                            data.getExtras().getString( "imagen" ),
                            genero,
                            data.getExtras().getBoolean( "leido" ),
                            data.getExtras().getString( "reseña" ),
                            data.getExtras().getFloat("puntuacion"));

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
            Toast.makeText(this, data.getExtras().getString("imagen"),Toast.LENGTH_LONG).show();
        }

        return;
    }


    private void inserta()
    {
        this.startActivityForResult( new Intent( this, NuevoLibroActivity.class ), RC_NUEVO_LIBRO );
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

    private void ver(int possition){
        Cursor cursor = this.adaptador.getCursor();
        cursor.moveToPosition(possition);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbManager.CAMPO_ID));
        SQLiteDatabase db = dbManager.getReadableDatabase();
        cursor = db.query(dbManager.TABLA_LIBROS, null, dbManager.CAMPO_ID+" = ?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.moveToFirst()){
            String titulo = cursor.getString(1);
            String autor = cursor.getString(2);
            String imagen = cursor.getString(3);
            String genero = cursor.getString(4);
            int leido = cursor.getInt(5);
            Intent myIntent = new Intent(ListaLibrosActivity.this,VerLibroActivity.class);
            myIntent.putExtra("titulo",titulo);
            myIntent.putExtra("autor",autor);
            myIntent.putExtra("imagen",imagen);
            myIntent.putExtra("genero",genero);
            myIntent.putExtra("leido",leido);
            if(leido == 1){
                String reseña = cursor.getString(6);
                String puntuacion = cursor.getString(7);
                myIntent.putExtra("reseña",reseña);
                myIntent.putExtra("puntuacion",puntuacion);
                Toast.makeText(ListaLibrosActivity.this, puntuacion, Toast.LENGTH_LONG).show();
            }


            //Toast.makeText(ListaLibrosActivity.this, puntuacion, Toast.LENGTH_LONG).show();
            ListaLibrosActivity.this.startActivity(myIntent);


        }else{
            Toast.makeText(ListaLibrosActivity.this, "ERROR!!", Toast.LENGTH_LONG).show();
        }





    }

    private SimpleCursorAdapter adaptador;
    //private ArrayList<Practica> practicas;
    private DBManager dbManager;
}
