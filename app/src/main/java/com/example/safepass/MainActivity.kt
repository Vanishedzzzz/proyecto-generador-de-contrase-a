package com.example.safepass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontrar los campos de entrada y el botón de registro
        val nombreEditText = findViewById<EditText>(R.id.Nombre)
        val contrasenaEditText = findViewById<EditText>(R.id.Contrasena)
        val registrarseButton = findViewById<Button>(R.id.Registrarse)
        val iniciarSesionButton = findViewById<Button>(R.id.buttonIniciarSesion)

        // Configurar el botón de registrarse para ir al RegisterActivity
        registrarseButton.setOnClickListener { v: View? ->
            val intent =
                Intent(
                    this@MainActivity,
                    RegisterActivity::class.java
                )
            startActivity(intent)
        }

        // Configurar el botón de iniciar sesión
        iniciarSesionButton.setOnClickListener { v: View? ->
            // Obtener los valores ingresados
            val nombre = nombreEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            // Validar los datos ingresados
            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Todos los campos son requeridos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Verificar los datos en SharedPreferences
            val sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )
            val storedName =
                sharedPreferences.getString(PREF_NAME, null)
            val storedPassword =
                sharedPreferences.getString(PREF_PASSWORD, null)

            // Verificar si los datos ingresados coinciden con los almacenados
            if (nombre == storedName && contrasena == storedPassword) {
                // Notificar al usuario que el inicio de sesión fue exitoso
                Toast.makeText(
                    this@MainActivity,
                    "Inicio de sesión exitoso",
                    Toast.LENGTH_SHORT
                ).show()

                // Ir a PasswordGeneratorActivity
                val intent =
                    Intent(
                        this@MainActivity,
                        PasswordGeneratorActivity::class.java
                    )
                startActivity(intent)
            } else {
                // Notificar al usuario que los datos son incorrectos
                Toast.makeText(
                    this@MainActivity,
                    "Nombre o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "UserPrefs"
        private const val PREF_NAME = "name"
        private const val PREF_EMAIL = "email"
        private const val PREF_PASSWORD = "password"
    }
}