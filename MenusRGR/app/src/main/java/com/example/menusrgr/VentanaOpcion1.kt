package com.example.toolbarymenupuntos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.menusrgr.R
import com.example.menusrgr.databinding.ActivityVentanaOpcion1Binding

class VentanaOpcion1 : AppCompatActivity() {
    lateinit var binding: ActivityVentanaOpcion1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_ventana_opcion1)
        binding = ActivityVentanaOpcion1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarOpcion1.title = "    Mi aplicación DAM"
        binding.toolbarOpcion1.subtitle = "     Opcion1"
        binding.toolbarOpcion1.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarOpcion1)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarOpcion1.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}