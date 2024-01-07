package com.example.ahorcado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    private EditText editTextJugador;
    private Button btnLogin;
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        editTextJugador = (EditText) findViewById(R.id.editTextJugador);
        btnLogin = findViewById(R.id.btnLogin);
        btnSalir = findViewById(R.id.btnSalir);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogIn.this, "Hasta pronto", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void iniciarSesion() {
        String jugador = editTextJugador.getText().toString();

        if (!jugador.isEmpty()) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intentMain = new Intent(LogIn.this, MainActivity.class);
            intentMain.putExtra("jugador", jugador);
            startActivity(intentMain);
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

}
