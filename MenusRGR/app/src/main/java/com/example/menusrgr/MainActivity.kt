package com.example.menusrgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.menusrgr.databinding.ActivityMainBinding
import Auxiliar.Conexion
import Modelo.Usuario
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var nombre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPrincipal.title = "Mi aplicación DAM"
        binding.toolbarPrincipal.subtitle = "Principal"
        binding.toolbarPrincipal.setLogo(R.drawable.ic_logo)

        setSupportActionBar(binding.toolbarPrincipal)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPrincipal.setNavigationOnClickListener {
            Toast.makeText(this, "Pulsado el retroceso", Toast.LENGTH_SHORT).show()
        }

        binding.btnAceptar.setOnClickListener {
            nombre = binding.txtUsuario.text.toString()
            if (nombre.isNotEmpty()) {
                var usuarioExistente:Usuario? = null
                // Buscar al usuario en la base de datos por su nombre
                usuarioExistente = Conexion.buscarUsuario(this, nombre.toString())

                if (usuarioExistente != null) {
                    // El usuario ya existe, ir a la VentanaOpcion2 con los datos
                    Toast.makeText(this, "¡Bienvenido de nuevo, $nombre!", Toast.LENGTH_SHORT).show()

                } else {
                    // El usuario no existe, crea un nuevo usuario con puntuación 0 y ve a VentanaOpcion2
                    var nuevoUsuario: Usuario
                    nuevoUsuario= Usuario(nombre, 0)
                    Conexion.addUsuario(this, nuevoUsuario)
                    Toast.makeText(this, "¡HOLA, $nombre!", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Por favor, ingrese su nombre.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnOp1 -> {
                irAVentanaOpcion1()
            }
            R.id.mnOp2 -> {
                // Solo ir a VentanaOpcion2, sin necesidad de un usuario actual
                irAVentanaOpcion2()
            }
            R.id.mnBusqueda -> {
                Toast.makeText(this, "Buscar", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val irAVentanaOpcion1: () -> Unit = {
        val miIntent = Intent(this, VentanaOpcion1::class.java)
        miIntent.putExtra("nombre", nombre)
        startActivity(miIntent)
    }

    private val irAVentanaOpcion2: () -> Unit = {
        val miIntent = Intent(this, VentanaOpcion2::class.java)
        miIntent.putExtra("nombre", nombre)
        startActivity(miIntent)
    }
}