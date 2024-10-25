package com.example.safepass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity {

    private TextView passwordPreview;
    private TextView textViewLength;
    private SeekBar seekBar;
    private CheckBox checkBoxUppercase, checkBoxSpecialChars;
    private Button btnGenerate, btnCopy, btnSave, btnViewSavedPasswords;

    private int passwordLength = 8;
    private final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String numbers = "0123456789";
    private final String specialChars = "!@#$%^&*()-_=+[]{}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generador);

        // Referencias a los elementos del layout
        passwordPreview = findViewById(R.id.passwordPreview);
        seekBar = findViewById(R.id.seekBar2);
        textViewLength = findViewById(R.id.textViewLength);
        checkBoxUppercase = findViewById(R.id.checkBoxUppercase);
        checkBoxSpecialChars = findViewById(R.id.checkBoxSpecialChars);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnCopy = findViewById(R.id.btnCopy);
        btnSave = findViewById(R.id.btnSave);

        // Botón para ver contraseñas guardadas
        btnViewSavedPasswords = findViewById(R.id.btnViewSavedPasswords);
        btnViewSavedPasswords.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordGeneratorActivity.this, ContrasActivity.class);
            startActivity(intent);
        });

        // Configurar el SeekBar
        seekBar.setMax(20);
        seekBar.setProgress(passwordLength);
        textViewLength.setText("Largo de contraseña: " + passwordLength);

        // Cambiar el largo de la contraseña cuando se mueve el SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordLength = progress;
                textViewLength.setText("Largo de contraseña: " + passwordLength);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botón para generar la contraseña
        btnGenerate.setOnClickListener(v -> generatePassword());

        // Botón para copiar la contraseña
        btnCopy.setOnClickListener(v -> copyPassword());

        // Botón para guardar la contraseña
        btnSave.setOnClickListener(v -> showSavePasswordDialog());
    }

    private void generatePassword() {
        StringBuilder password = new StringBuilder();
        String chars = lowerCaseLetters;

        if (checkBoxUppercase.isChecked()) {
            chars += upperCaseLetters;
        }
        if (checkBoxSpecialChars.isChecked()) {
            chars += specialChars;
        }
        chars += numbers;

        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        passwordPreview.setText(password.toString());
    }

    private void copyPassword() {
        String password = passwordPreview.getText().toString();
        if (!password.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSavePasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_save_password);
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> {
            String password = passwordPreview.getText().toString();
            String name = editTextName.getText().toString();
            if (!name.isEmpty() && !password.isEmpty()) {
                Intent intent = new Intent(PasswordGeneratorActivity.this, ContrasActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("password", password);
                startActivity(intent);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
