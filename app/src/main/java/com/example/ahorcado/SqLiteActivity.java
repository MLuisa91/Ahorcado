package com.example.ahorcado;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqLiteActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText editTextPalabra;
    private EditText editTextDefinicion;
    private ListView lVpalabras;
    private BaseDeDatosPalabras datos;
    private Palabra palabraSeleccionada;
    private ListViewPalabrasAdapter adapter;
    List<Palabra> listaPalabras;
    Partida  partida;
    private String jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_activity);
        spinner = (Spinner) findViewById(R.id.spinnerOpciones);
        cargarSpinner();
        editTextPalabra = (EditText) findViewById(R.id.editTextPalabra);
        editTextPalabra.setTextSize(20);
        editTextDefinicion = (EditText) findViewById(R.id.editTextTextMultiLineDefinicion);
        editTextDefinicion.setTextSize(20);
        lVpalabras = (ListView) findViewById(R.id.listViewPalabras);
        partida = (Partida) getIntent().getSerializableExtra("partida");
        jugador = (String) getIntent().getSerializableExtra("jugador");
        datos = new BaseDeDatosPalabras(this);
        cargaPalabras();
        lVpalabras.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarPalabras(position);
            }
        });
    }

    private void mostrarPalabras(int position) {
        palabraSeleccionada = listaPalabras.get(position);

        editTextPalabra.setText(palabraSeleccionada.getNombre());
        editTextDefinicion.setText(palabraSeleccionada.getDescripcion());
    }

    private void cargarSpinner() {
        ArrayAdapter<CharSequence> adapterChar = ArrayAdapter.createFromResource(this, R.array.acciones, android.R.layout.simple_spinner_item);
        adapterChar.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapterChar);
    }

    public void realizarAccion(View vista){
        String seleccion = spinner.getSelectedItem().toString();
        switch (seleccion){
            case "Insertar":
                opcionInsertar();
                break;
            case "Eliminar":
                opcionEliminar();
                break;
            case "Modificar":
                opcionModificar();
                break;
            case "Listar":
                opcionListar();
                break;
            default:
                Toast.makeText(this,"Es obligatorio seleccionar la acción.",Toast.LENGTH_SHORT).show();
        }
    }

    private void opcionListar() {
        Intent intentListar = new Intent(SqLiteActivity.this, ListarPalabrasActivity.class);
        intentListar.putExtra("partida",partida);
        startActivity(intentListar);
    }

    private void opcionInsertar() {
        String palabra = editTextPalabra.getText().toString();
        String definicion = editTextDefinicion.getText().toString();

        if(!validarPalabra(palabra)){
            Toast.makeText(this,"La palabra debe tener maximo 10 caracteres y no puede contener espacios vacíos ni caracteres especiales.",Toast.LENGTH_SHORT).show();
        }else if (validaDuplicado(palabra)){
            Toast.makeText(this,"La palabra ya existe en la base de datos.",Toast.LENGTH_SHORT).show();
        }else{
            if (!palabra.isEmpty() && !definicion.isEmpty()){
                SQLiteDatabase db = datos.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(BaseDeDatosPalabras.COLUMN_PALABRA, palabra);
                values.put(BaseDeDatosPalabras.COLUMN_DEFINICION, definicion);
                long nuevaPalabra = db.insert(BaseDeDatosPalabras.TABLE_NAME, null, values);
                limpiarFormulario();
                if(nuevaPalabra>0) {
                    cargaPalabras();
                    Toast.makeText(this, "La palabra ha sido guardada.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Error,no se ha podido guardar la nueva palabra.",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }else{
                Toast.makeText(this,"Error,no se ha podido guardar la nueva palabra.",Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void opcionEliminar() {

        SQLiteDatabase db = datos.getWritableDatabase();

        if(palabraSeleccionada!=null){
            db.delete(BaseDeDatosPalabras.TABLE_NAME, BaseDeDatosPalabras.COLUMN_ID + "=?",new String[]{palabraSeleccionada.getId().toString()});
            cargaPalabras();
            limpiarFormulario();

            Toast.makeText(this,"La palabra ha sido eliminada.",Toast.LENGTH_SHORT).show();
            db.close();
        }else{
            Toast.makeText(this,"Error, la palabra no ha sido eliminada.",Toast.LENGTH_SHORT).show();
        }

    }

    private void opcionModificar() {
        String palabra = editTextPalabra.getText().toString();
        String definicion = editTextDefinicion.getText().toString();
        if(!validarPalabra(palabra)){
            Toast.makeText(this,"La palabra debe tener maximo 10 caracteres y no puede contener espacios vacíos ni caracteres especiales.",Toast.LENGTH_SHORT).show();
        }else if (validaDuplicado(palabra)){
            Toast.makeText(this,"La palabra ya existe en la base de datos.",Toast.LENGTH_SHORT).show();
        }else{
            if (!palabra.isEmpty() && !definicion.isEmpty()) {
                SQLiteDatabase db = datos.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(BaseDeDatosPalabras.COLUMN_PALABRA, palabra);
                values.put(BaseDeDatosPalabras.COLUMN_DEFINICION, definicion);
                db.update(BaseDeDatosPalabras.TABLE_NAME, values, BaseDeDatosPalabras.COLUMN_ID + "=?", new String[]{palabraSeleccionada.getId().toString()});
                limpiarFormulario();

                cargaPalabras();
                Toast.makeText(this, "La palabra ha sido modificada.", Toast.LENGTH_SHORT).show();
                db.close();
            } else {
                Toast.makeText(this, "Error, la palabra no ha sido modificada.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void volverMain(View vista){
        Intent intentVolver = new Intent(vista.getContext(), MainActivity.class);
        intentVolver.putExtra("partida", partida);
        intentVolver.putExtra("jugador", jugador);
        startActivity(intentVolver);
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
        lVpalabras.setAdapter(adapter);

    }

    public boolean validaDuplicado(String palabra){
        boolean duplicada =  false;
        SQLiteDatabase db = datos.getReadableDatabase();
        // importante el array de String es donde se indican las columnas que va a devolver la selectd
        Cursor cursor = db.query(BaseDeDatosPalabras.TABLE_NAME, new String[]{BaseDeDatosPalabras.COLUMN_ID,
                BaseDeDatosPalabras.COLUMN_PALABRA, BaseDeDatosPalabras.COLUMN_DEFINICION}, BaseDeDatosPalabras.COLUMN_PALABRA + "=?", new String[]{palabra}, null, null, null);
        if(cursor.getCount()>0){
            duplicada = true;
        }
        cursor.close();
        db.close();
        return duplicada;
    }

    public void limpiarFormulario(){
        editTextPalabra.getText().clear();
        editTextDefinicion.getText().clear();
    }

    /**
     * Método para validar que solo nos metan palabras
     * @param palabra
     * @return
     */
    public boolean validarPalabra(String palabra){
        Pattern p = Pattern.compile("[a-zA-Z]{1,11}");
        Matcher m = p.matcher(palabra);

        if (m.matches()) {
            return true;
        }

        return false;
    }
}
