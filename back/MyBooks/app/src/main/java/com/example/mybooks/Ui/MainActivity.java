package com.example.mybooks.Ui;

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

import com.example.mybooks.R;
import com.example.mybooks.Core.DBManager;
import com.example.mybooks.Core.Libro;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static int RC_NUEVO_LIBRO = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listeners
        final Button BT_LISTA_LIBROS = this.findViewById(R.id.btListaLibros);
        final Button BT_INSERTA_PRINCIPAL = this.findViewById(R.id.btInsertaPrincipal);

        BT_LISTA_LIBROS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.llamarLista();
            }
        });

        BT_INSERTA_PRINCIPAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                ;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void llamarLista() {
        this.startActivity(new Intent(this, ListaLibrosActivity.class));
    }

}
