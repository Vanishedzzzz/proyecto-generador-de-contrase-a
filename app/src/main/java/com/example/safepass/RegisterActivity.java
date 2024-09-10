package com.example.safepass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse); // Asegúrate de que el nombre del archivo XML sea 'registrarse.xml'

        // Encontrar los campos de entrada y el botón
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        // Configurar el botón de registrarse
        registerButton.setOnClickListener(v -> {
            // Obtener los valores ingresados
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validar los datos ingresados
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar los datos en SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_NAME, name);
            editor.putString(PREF_EMAIL, email);
            editor.putString(PREF_PASSWORD, password);
            editor.apply(); // Aplicar los cambios

            // Notificar al usuario que el registro fue exitoso
            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // Volver al MainActivity para iniciar sesión
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Para cerrar la actividad actual y no regresar a ella
        });

        // Configurar el botón para ir al MainActivity si ya tienes cuenta
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Para cerrar la actividad actual y no regresar a ella
        });
    }
}
