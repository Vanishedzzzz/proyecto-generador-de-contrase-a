package com.example.safepass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContrasActivity extends AppCompatActivity {

    private LinearLayout layoutContras;
    private ArrayList<String> passwordsList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contras);  // Carga el layout contras.xml

        layoutContras = findViewById(R.id.linearLayoutContraseñas);
        passwordsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("SafePassPrefs", MODE_PRIVATE); // Crear SharedPreferences

        // Recuperar contraseñas guardadas
        loadPasswords();

        // Recuperar datos del Intent
        String name = getIntent().getStringExtra("name");
        String password = getIntent().getStringExtra("password");

        // Agregar la contraseña si no es nula
        if (name != null && password != null) {
            addPassword(name, password);
            savePassword(name, password); // Guardar la contraseña en SharedPreferences
        }

        // Configurar el botón "Volver al Generador"
        Button btnBackToGenerator = findViewById(R.id.btnBackToGenerator);
        btnBackToGenerator.setOnClickListener(v -> {
            Intent intent = new Intent(ContrasActivity.this, PasswordGeneratorActivity.class);
            startActivity(intent);
            finish(); // Cierra ContrasActivity si no quieres regresar a ella
        });

        // Configurar el botón "Copiar Contraseñas"
        Button btnCopyPasswords = findViewById(R.id.btnCopyPasswords);
        btnCopyPasswords.setOnClickListener(v -> {
            copyAllPasswordsToClipboard();
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
        nameTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));

        // Subtítulo con la contraseña
        TextView passwordTextView = new TextView(this);
        passwordTextView.setText(password);
        passwordTextView.setTextSize(16);
        passwordTextView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        // Agregar los TextViews al contenedor
        passwordContainer.addView(nameTextView);
        passwordContainer.addView(passwordTextView);

        // Agregar el contenedor de la contraseña al layout principal
        layoutContras.addView(passwordContainer);

        // Agregar la contraseña a la lista
        passwordsList.add(name + ":" + password);
    }

    // Método para copiar todas las contraseñas al portapapeles
    private void copyAllPasswordsToClipboard() {
        StringBuilder allPasswords = new StringBuilder();

        for (String password : passwordsList) {
            allPasswords.append(password).append("\n"); // Agregar cada contraseña y un salto de línea
        }

        if (allPasswords.length() > 0) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Contraseñas", allPasswords.toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Contraseñas copiadas al portapapeles", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay contraseñas para copiar", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para guardar una contraseña en SharedPreferences
    private void savePassword(String name, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String existingPasswords = sharedPreferences.getString("passwords", "");
        String newPasswords = existingPasswords + name + ":" + password + ";";
        editor.putString("passwords", newPasswords);
        editor.apply();
    }

    // Método para cargar las contraseñas de SharedPreferences
    private void loadPasswords() {
        String savedPasswords = sharedPreferences.getString("passwords", "");
        if (!savedPasswords.isEmpty()) {
            List<String> savedPasswordsList = Arrays.asList(savedPasswords.split(";"));
            for (String savedPassword : savedPasswordsList) {
                if (!savedPassword.isEmpty()) {
                    String[] parts = savedPassword.split(":");
                    if (parts.length == 2) {
                        addPassword(parts[0], parts[1]);
                    }
                }
            }
        }
    }
}
