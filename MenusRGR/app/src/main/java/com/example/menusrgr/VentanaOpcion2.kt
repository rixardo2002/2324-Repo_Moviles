package com.example.menusrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.menusrgr.databinding.ActivityVentanaOpcion2Binding
class VentanaOpcion2 : AppCompatActivity() {
    lateinit var binding: ActivityVentanaOpcion2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_ventana_opcion1)
        binding = ActivityVentanaOpcion2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarOpcion2.title = "    Mi aplicación DAM"
        binding.toolbarOpcion2.subtitle = "     Opcion1"
        binding.toolbarOpcion2.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarOpcion2)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarOpcion2.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}