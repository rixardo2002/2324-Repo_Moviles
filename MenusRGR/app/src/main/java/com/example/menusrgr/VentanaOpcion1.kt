package com.example.menusrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.menusrgr.databinding.ActivityVentanaOpcion1Binding

class VentanaOpcion1 : AppCompatActivity() {
    lateinit var binding: ActivityVentanaOpcion1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_ventana_opcion1)
        binding = ActivityVentanaOpcion1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar2.title = "    Mi aplicación DAM"
        binding.toolbar2.subtitle = "     Opcion1"
        binding.toolbar2.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbar2)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar2.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Recupera el nombre enviado desde MainActivity
        val nombre = intent.getStringExtra("nombre")

        if (nombre != null) {
            // Hacer algo con el nombre, como mostrarlo en un TextView
            Toast.makeText(this,"Bienvenido "+ nombre, Toast.LENGTH_SHORT).show()

        }
    }
}