package com.example.menu_con_alumnos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.menu_con_alumnos.databinding.ActivityMainBinding


lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPrincipal.title = "    Mi aplicación DAM2T"
        binding.toolbarPrincipal.subtitle = "     inicio"
        binding.toolbarPrincipal.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarPrincipal)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPrincipal.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
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
                //SirAVentanaOpcion1()
                Toast.makeText(this, "Opción 1", Toast.LENGTH_LONG).show()
            }
            R.id.mnOp2 -> {
                Toast.makeText(this, "Opción 2", Toast.LENGTH_SHORT).show()
            }

            R.id.mnBusqueda -> {
                Toast.makeText(this, "Buscar", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}