package com.example.mybooks.Core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    private static int Version = 5;
    private static String Nombre = "libros";
    private static String LOG_TAG = "ORM";
    private static String TABLA_LIBROS = "libros";
    public static String CAMPO_ID = "_id";
    public static String CAMPO_TITULO = "titulo";
    public static String CAMPO_AUTOR = "autor";
    public static String CAMPO_IMAGEN = "imagen";
    public static String CAMPO_GENERO = "genero";
    public static String CAMPO_LEIDO = "leido";
    public static String CAMPO_RESENHA = "reseña";
    public static String CAMPO_PUNTUACION = "puntuacion";

    public DBManager(Context c)
    {
        super( c, Nombre, null, Version );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            db.beginTransaction();

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLA_LIBROS + "("
                            + CAMPO_ID + " integer PRIMARY KEY NOT NULL,"
                            + CAMPO_TITULO + " string(80) NOT NULL,"
                            + CAMPO_AUTOR + " string(80) NOT NULL,"
                            + CAMPO_IMAGEN + " string(255),"
                            + CAMPO_GENERO + " string(80) NOT NULL,"
                            + CAMPO_LEIDO + " integer NOT NULL,"
                            + CAMPO_RESENHA + " string(255),"
                            + CAMPO_PUNTUACION + " real)"
            );

            db.setTransactionSuccessful();
        }
        catch(SQLException exc) {
            Log.e( LOG_TAG, "creando base de datos: " + exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try {
            db.beginTransaction();

            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_LIBROS );

            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e(LOG_TAG, "eliminando la base de datos: " + exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }

    public void guarda(Libro l)
    {
        final SQLiteDatabase DB = this.getWritableDatabase();
        final ContentValues VALUES = new ContentValues();

        // Asignar los valores de los datos
        VALUES.put( CAMPO_TITULO, l.getTitulo() );
        VALUES.put( CAMPO_AUTOR, l.getAutor() );
        VALUES.put( CAMPO_IMAGEN, l.getImagen());
        VALUES.put( CAMPO_GENERO, l.getGenero().toString());
        int leido = l.isLeido() ? 1 : 0;
        VALUES.put( CAMPO_LEIDO, leido);
        if(l.isLeido()) {
            VALUES.put( CAMPO_RESENHA, l.getReseña());
            VALUES.put( CAMPO_PUNTUACION, l.getPuntuacion());
        }
        try {
            DB.beginTransaction();
            DB.insert( TABLA_LIBROS, null, VALUES );
            DB.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( LOG_TAG, "guardando libro: " + exc.getMessage() );
        } finally {
            DB.endTransaction();
        }

        return;
    }

    public void borra(int id)
    {
        final SQLiteDatabase DB = this.getWritableDatabase();

        try {
            DB.beginTransaction();
            Log.i( LOG_TAG, "deleting row with id: " + id );

            DB.delete(
                    TABLA_LIBROS,
                    CAMPO_ID + "=?",
                    new String[]{ Integer.toString( id ) } );

            DB.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( LOG_TAG, "borrando id: " + id + ": " + exc.getMessage() );
        } finally {
            DB.endTransaction();
        }

    }

    public Cursor getAllCursor()
    {
        return this.getReadableDatabase().query(
                TABLA_LIBROS,
                null, null, null, null, null, null
        );
    }
}
