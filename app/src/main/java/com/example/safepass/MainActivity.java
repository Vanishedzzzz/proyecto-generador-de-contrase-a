package com.example.safepass;

import android.content.Intent;
import android.content.SharedPreferences; // Asegúrate de importar esta clase
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String PREF_NAME = "name";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Encontrar los campos de entrada y el botón de registro
        EditText nombreEditText = findViewById(R.id.Nombre);
        EditText contrasenaEditText = findViewById(R.id.Contrasena);
        Button registrarseButton = findViewById(R.id.Registrarse);
        Button iniciarSesionButton = findViewById(R.id.buttonIniciarSesion);

        // Configurar el botón de registrarse para ir al RegisterActivity
        registrarseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el botón de iniciar sesión
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados
                String nombre = nombreEditText.getText().toString();
                String contrasena = contrasenaEditText.getText().toString();

                // Validar los datos ingresados
                if (nombre.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar los datos en SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String storedName = sharedPreferences.getString(PREF_NAME, ""); // Cambia null por ""
                String storedPassword = sharedPreferences.getString(PREF_PASSWORD, ""); // Cambia null por ""

                // Verificar si los datos ingresados coinciden con los almacenados
                if (nombre.equals(storedName) && contrasena.equals(storedPassword)) {
                    // Notificar al usuario que el inicio de sesión fue exitoso
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Ir a PasswordGeneratorActivity
                    Intent intent = new Intent(MainActivity.this, PasswordGeneratorActivity.class);
                    startActivity(intent);
                } else {
                    // Notificar al usuario que los datos son incorrectos
                    Toast.makeText(MainActivity.this, "Nombre o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
