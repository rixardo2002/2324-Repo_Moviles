package com.example.menusrgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.menusrgr.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var nombre:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPrincipal.title = "    Mi aplicación DAM"
        binding.toolbarPrincipal.subtitle = "     Principal"
        binding.toolbarPrincipal.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarPrincipal)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPrincipal.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
        }
        // Agregar un OnClickListener al botón "Aceptar"
        binding.btnAceptar.setOnClickListener {
             nombre = binding.txtUsuario.text.toString()
            if (nombre.isNotEmpty()) {
                // Aquí puedes hacer algo con el nombre, como mostrarlo en un Toast
                Toast.makeText(this, "Hola, $nombre", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingrese su nombre.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //************************* Funciones auxiliares para los menú de puntos *****************************
    //la primera infla el menú que previamente hemos creado como resource, la segunda carga las opciones.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnOp1 -> {
                irAVentanaOpcion1()

            }
            R.id.mnOp2 -> {
                irAVentanaOpcion2()

            }

            R.id.mnBusqueda -> {
                Toast.makeText(this, "Buscar", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /*private fun irAVentanaOpcion1() {

        var miIntent: Intent = Intent(this, VentanaOpcion1::class.java)
        startActivity(miIntent)
    }*/
    //version lambda de la funcion. Unit equivale a void de Java.
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