package com.example.ahorcado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Ranking extends AppCompatActivity {

    private Partida partida;
    private String jugador;
    private TableLayout tableLayoutRanking;
    private BaseDeDatosRanking puntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        partida = (Partida) getIntent().getSerializableExtra("partida");
        jugador = (String) getIntent().getSerializableExtra("jugador");
        puntuaciones = new BaseDeDatosRanking(this);
        tableLayoutRanking = findViewById(R.id.tableLayoutRanking);
        mostrarDatosEnTabla();
    }

    private void mostrarDatosEnTabla() {
        SQLiteDatabase db = puntuaciones.getReadableDatabase();

        // Consulta para obtener las puntuaciones ordenadas por partida
        Cursor cursor = db.query(puntuaciones.TABLE_NAME,
                new String[]{puntuaciones.COLUMN_JUGADOR, puntuaciones.COLUMN_PUNTUACION},
                null, null, null, null, puntuaciones.COLUMN_PUNTUACION + " DESC");

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String jugador = cursor.getString(cursor.getColumnIndex(puntuaciones.COLUMN_JUGADOR));
            @SuppressLint("Range") int puntos = cursor.getInt(cursor.getColumnIndex(puntuaciones.COLUMN_PUNTUACION));

            // Nueva fila en la tabla y añadir los datos
            TableRow newRow = new TableRow(this);

            TextView tvJugador = new TextView(this);
            tvJugador.setText(jugador);
            tvJugador.setPadding(8, 8, 8, 8);
            newRow.addView(tvJugador);

            TextView tvPuntuacion = new TextView(this);
            tvPuntuacion.setText(String.valueOf(puntos));
            tvPuntuacion.setPadding(8, 8, 8, 8);
            newRow.addView(tvPuntuacion);

            tableLayoutRanking.addView(newRow);
        }

        cursor.close();
        db.close();
    }

    public void volverMain(View vista){
        Intent intentVolver = new Intent(vista.getContext(), MainActivity.class);
        intentVolver.putExtra("partida", partida);
        intentVolver.putExtra("jugador", jugador);
        startActivity(intentVolver);
    }

}