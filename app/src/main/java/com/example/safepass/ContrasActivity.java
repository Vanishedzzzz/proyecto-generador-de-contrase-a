package com.example.safepass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContrasActivity extends AppCompatActivity {

    private LinearLayout layoutContras;
    private ArrayList<String> passwordsList;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_PASSWORDS = "passwords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contras);  // Carga el layout contras.xml

        layoutContras = findViewById(R.id.linearLayoutContraseñas); // Asegúrate que este ID existe en contras.xml
        passwordsList = new ArrayList<>();

        // Cargar contraseñas desde SharedPreferences
        loadPasswords();

        // Recuperar datos del Intent
        String name = getIntent().getStringExtra("name");
        String password = getIntent().getStringExtra("password");

        // Agregar la contraseña si no es nula
        if (name != null && password != null) {
            addPassword(name, password);
        }

        // Configurar el botón "Volver al Generador"
        Button btnBackToGenerator = findViewById(R.id.btnBackToGenerator);
        btnBackToGenerator.setOnClickListener(v -> {
            Intent intent = new Intent(ContrasActivity.this, PasswordGeneratorActivity.class);
            startActivity(intent);
            finish(); // Cierra ContrasActivity si no quieres regresar a ella
        });
    }

    // Método para agregar la contraseña al layout
    public void addPassword(String name, String password) {
        // Crear un nuevo contenedor para la contraseña
        LinearLayout passwordContainer = new LinearLayout(this);
        passwordContainer.setOrientation(LinearLayout.VERTICAL);
        passwordContainer.setBackgroundResource(R.drawable.border); // Asumiendo que tienes un drawable llamado border
        passwordContainer.setPadding(16, 16, 16, 16);
        passwordContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Título con el nombre
        TextView nameTextView = new TextView(this);
        nameTextView.setText(name);
        nameTextView.setTextSize(20);
        nameTextView.setTextColor(getResources().getColor(android.R.color.black));

        // Subtítulo con la contraseña
        TextView passwordTextView = new TextView(this);
        passwordTextView.setText(password);
        passwordTextView.setTextSize(16);
        passwordTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Agregar los TextViews al contenedor
        passwordContainer.addView(nameTextView);
        passwordContainer.addView(passwordTextView);

        // Agregar el contenedor de la contraseña al layout principal
        layoutContras.addView(passwordContainer);

        // Agregar la contraseña a la lista y guardarla en SharedPreferences
        passwordsList.add(name + ":" + password);
        savePasswords();
    }

    // Método para guardar las contraseñas en SharedPreferences
    private void savePasswords() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>(passwordsList);
        editor.putStringSet(KEY_PASSWORDS, set);
        editor.apply();
    }

    // Método para cargar las contraseñas desde SharedPreferences
    private void loadPasswords() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(KEY_PASSWORDS, new HashSet<>());
        passwordsList.clear();
        passwordsList.addAll(set);

        // Mostrar contraseñas almacenadas
        for (String entry : passwordsList) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                addPassword(parts[0], parts[1]);
            }
        }
    }
}
