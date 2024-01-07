package com.example.ahorcado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tVPalabra;
    private String palabra;
    private EditText editTextLetra;
    private String letraIntentada;
    private TextView intentosRestantes;
    private int intentos = 5;
    private final Random random = new Random();
    private View vista;
    private List<String> introducidas = new ArrayList<>();
    char[] palabraOculta;
    private BaseDeDatosPalabras datos;
    private BaseDeDatosRanking puntuaciones;
    private Partida partida;
    private String jugador;
    private TextView tVJugador;
    private int puntos = 0;
    private TextView tVPuntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tVPalabra = (TextView) findViewById(R.id.textViewPalabra);
        tVPalabra.setTextSize(60);
        tVPalabra.setPadding(10,5,10,5);
        tVJugador = (TextView) findViewById(R.id.textViewJugador);
        tVPuntos = (TextView) findViewById(R.id.textViewContadorPuntos);
        editTextLetra = (EditText) findViewById(R.id.editTextLetraUsuario);
        intentosRestantes = (TextView) findViewById(R.id.textViewContadorIntentos);
        intentosRestantes.setTextSize(30);
        datos = new BaseDeDatosPalabras(this);
        puntuaciones = new BaseDeDatosRanking(this);
        partida = (Partida) getIntent().getSerializableExtra("partida");
        if(partida != null) {
            palabra = partida.getPalabra();
            introducidas = partida.getIntroducidas();
            intentos = partida.getIntentos();
            palabraOculta = partida.getPalabraOculta();
            intentosRestantes.setText(String.valueOf(partida.getIntentos()));
            repintarTexto();
        }
        jugador = (String) getIntent().getSerializableExtra("jugador");
        if(!jugador.isEmpty()){
            tVJugador.setText(jugador);
        }
        tVPuntos.setText(String.valueOf(puntos));
    }

    // en este método inicializamos los diferentes parámetros de la aplicación
    public void mostrarPalabra(View vista){
        palabra = obtenerPalabraAleatoria();
        introducidas.clear();
        intentos = 5;
        intentosRestantes.setText(String.valueOf(intentos));
       // int indice = random.nextInt(listaPalabras.length);
        //palabra = listaPalabras[indice];
        palabraOculta = new char[palabra.length()];
        // inicializamos el array a guiones
        for (int i=0;i<palabra.length();i++){
            palabraOculta[i]='_';
        }
        repintarTexto();
    }

    @SuppressLint("Range")
    //Método para obtener una palabra aleatoria de la base de datos y utilizarla para jugar
    private String obtenerPalabraAleatoria() {
        SQLiteDatabase db = datos.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + BaseDeDatosPalabras.COLUMN_PALABRA + " FROM "
        + BaseDeDatosPalabras.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);
        String palabra = "";

        if(cursor.moveToFirst()){
            palabra = cursor.getString(cursor.getColumnIndex(BaseDeDatosPalabras.COLUMN_PALABRA));
        }
        cursor.close();
        db.close();

        return palabra;
    }

    /**
     * Para repintar la palabra en la pantalla
     */
    public void repintarTexto(){
        String textoPalabra = "";
        for(int i = 0; i < palabra.length();i++){
            textoPalabra = textoPalabra + palabraOculta[i];
        }
        tVPalabra.setText(textoPalabra);
    }

    public void comprobarLetra(View vista){
        int contador = 0;
        String palabraFinal = "";
        boolean encontrada = false;
        char[] letras = palabra.toCharArray();
        String palabraParaAdivinar = palabra.toLowerCase();
        letraIntentada = editTextLetra.getText().toString();

        if(letraIntentada==null || letraIntentada.equals("")){
            Toast.makeText(vista.getContext(),"Necesita introducir una letra",Toast.LENGTH_LONG).show();
        }else{
            if (introducidas.contains(letraIntentada.toLowerCase())) {
                Toast.makeText(vista.getContext(),"Letra repetida",Toast.LENGTH_LONG).show();
            }
            // nos recorremos el array de letras que tiene la palabra y la vamos comparando con la letra introducida en pantalla
            for(char letra:letras){
                if(letraIntentada.equalsIgnoreCase(Character.toString(letra))){
                    // si la encontramos sustituimos la letra por la posición en el array de palabra oculta
                    palabraOculta[contador] = letra;
                    encontrada=true;
                }
                palabraFinal = palabraFinal + palabraOculta[contador];
                contador++;
            }

            // si no encontramos la letra introducido restamos uno al contador de intentos y pintamos los intentos en pantalla
            if(!encontrada){
                intentos--;
                intentosRestantes.setText(String.valueOf(intentos));
                if(puntos > 0){
                    puntos = puntos - 2;
                    tVPuntos.setText(String.valueOf(puntos));
                }
            }else{
                // si adivinamos la letra repintamos el texto en pantalla
                repintarTexto();
                puntos = puntos + 5;
                tVPuntos.setText(String.valueOf(puntos));
            }
            // si intentos llegan a 0 entonces mostramos un dialog con el mensaje de has perdido
            if(intentos==0){
                crearDialog("Lo siento, has perdido");
                introducidas.clear();
                SQLiteDatabase db = puntuaciones.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put(BaseDeDatosRanking.COLUMN_JUGADOR, jugador);
                values.put(BaseDeDatosRanking.COLUMN_PUNTUACION, puntos);

                db.insert(BaseDeDatosRanking.TABLE_NAME, null, values);
                db.close();
                puntos = 0;
            }
            // si adivinamos la palabra antes de terminar todos los intentos mostramos el mensaje de ganador
            if(palabraParaAdivinar.equalsIgnoreCase(palabraFinal) && intentos!=0){
                crearDialog("Felicidades, has ganado");
                puntos = puntos + 50;
                tVPuntos.setText(String.valueOf(puntos));
                introducidas.clear();
            }

            // una vez introducida la letra por pantalla la metemos en un array de letras intentadas y limpiamos el campo de texto donde introducimos las letras
            introducidas.add(letraIntentada.toLowerCase());
            editTextLetra.setText("");
        }


    }

    /*
    Este método los sirve para pintar los dialog de la aplicación
     */
    private void crearDialog(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mostrarPalabra(vista);
                }
                 })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(vista.getContext(),"Hasta pronto",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mostrarPalabra:
                mostrarPalabraActual();
                return true;
            case R.id.menu_Ranking:
                mostrarRanking();
                return true;
            case R.id.SQLite:
                mostrarPalabras();
                return true;
            case R.id.menu_salir:
                salirDeLaAplicacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarRanking() {
        Intent intentRanking = new Intent(MainActivity.this, Ranking.class);
        intentRanking.putExtra("partida",partida);
        intentRanking.putExtra("jugador", jugador);
        startActivity(intentRanking);
    }

    private void mostrarPalabraActual() {
        Toast.makeText(MainActivity.this, "La palabra con la que estás jugando es: " + palabra, Toast.LENGTH_SHORT).show();
    }

    private void mostrarPalabras() {
        partida = new Partida(palabra,palabraOculta,intentos,introducidas);
        Intent intent = new Intent(MainActivity.this, SqLiteActivity.class);
        intent.putExtra("partida", partida);
        intent.putExtra("jugador", jugador);
        startActivity(intent);

    }
//cambiar para que vuelva a la pantalla LogIn
    private void salirDeLaAplicacion() {
        //finish(); El finish está en la boton salir de LogIn
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);

    }

}