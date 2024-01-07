package com.example.ahorcado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListarPalabrasActivity extends AppCompatActivity {

    private ListView listView;
    private BaseDeDatosPalabras datos;
    private ListViewPalabrasAdapter adapter;
    List<Palabra> listaPalabras;
    private Palabra palabraSeleccionada;
    private Partida partida;
    private String jugador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_palabras);
        listView = (ListView) findViewById(R.id.listView);
        datos = new BaseDeDatosPalabras(this);
        partida = (Partida) getIntent().getSerializableExtra("partida");
        jugador = (String) getIntent().getSerializableExtra("jugador");
        cargaPalabras();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDefinicion(position);
            }
        });
    }

    private void mostrarDefinicion(int position) {
        palabraSeleccionada = listaPalabras.get(position);
        if(palabraSeleccionada!=null){
            Toast.makeText(this, palabraSeleccionada.getDescripcion(),Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("Range")
    private void cargaPalabras(){
        listaPalabras = new ArrayList<>();
        SQLiteDatabase db = datos.getReadableDatabase();
        // importante el array de String es donde se indican las columnas que va a devolver la selectd
        Cursor cursor = db.query(BaseDeDatosPalabras.TABLE_NAME, new String[]{BaseDeDatosPalabras.COLUMN_ID,
                BaseDeDatosPalabras.COLUMN_PALABRA, BaseDeDatosPalabras.COLUMN_DEFINICION}, null, null, null, null, null);
        while(cursor.moveToNext()){
            listaPalabras.add(new Palabra(cursor.getInt(cursor.getColumnIndex(BaseDeDatosPalabras.COLUMN_ID))
                    ,cursor.getString(cursor.getColumnIndex(BaseDeDatosPalabras.COLUMN_PALABRA)),
                    cursor.getString(cursor.getColumnIndex(BaseDeDatosPalabras.COLUMN_DEFINICION))));
        }
        cursor.close();
        db.close();
        //Adapter personalizado del tipo ListViewPalabrasAdapter
        adapter = new ListViewPalabrasAdapter(this, listaPalabras);
        listView.setAdapter(adapter);

    }

    public void volverMain(View vista){
        Intent intentVolver = new Intent(vista.getContext(), SqLiteActivity.class);
        intentVolver.putExtra("partida", partida);
        intentVolver.putExtra("jugador",jugador);
        startActivity(intentVolver);
    }

}
