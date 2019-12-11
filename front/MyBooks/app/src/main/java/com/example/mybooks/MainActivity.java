package com.example.mybooks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_create);

        //Button btEjercicios = (Button) this.findViewById( R.id.button1);
        //Button btRutina = (Button) this.findViewById( R.id.button2);
        //final ListView LV_PRACTICAS = this.findViewById(R.id.book_list);

        /*this.itemsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_selectable_list_item, items);


        for (int i = 0; i<=10; i++){
            items.add("ITEM "+i);
        }


        ListView listView =  findViewById(R.id.list);
        listView.setAdapter(this.itemsAdapter);*/

        //AÑADIR DESCRIPCION Y ESTRELLAS
        Switch simpleSwitch = (Switch) findViewById(R.id.switchRead);
        simpleSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Buscamos los elementos necesarios
                Switch aSwitch = findViewById(R.id.switchRead);
                RatingBar ratingBar = findViewById(R.id.ratingBar);
                EditText description = findViewById(R.id.bookDescription);
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

        //CONTROLAR VALOR ESTRELLAS

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(MainActivity.this ,"Ha valorado con: "+ rating, Toast.LENGTH_LONG).show();
            }
        });

    }

    /*private ArrayAdapter<String> itemsAdapter = null;
    private List<String> items = new ArrayList<String>();*/
}
