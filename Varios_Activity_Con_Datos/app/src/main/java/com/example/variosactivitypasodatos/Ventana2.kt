package com.example.variosactivitypasodatos

import Modelo.AlmacenPersonas
import Modelo.Persona
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.variosactivitypasodatos.databinding.ActivityMainBinding
import com.example.variosactivitypasodatos.databinding.ActivityVentana2Binding

class Ventana2 : AppCompatActivity() {

    lateinit var binding: ActivityVentana2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVentana2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var nombre = intent.getStringExtra("nombre")
        var edad = intent.getStringExtra("edad")
        var persona:Persona = Persona(nombre,edad)
        binding.cajaNombre.setText(nombre)
        binding.cajaEdad.setText(edad)



        AlmacenPersonas.aniadirPersona(persona)
        var cadena: String = ""
        var i:Int=1
        for (p in AlmacenPersonas.personas){
            cadena+=""+i+". "+p.nombre+" "+p.edad +"\n"
            i++
            binding.multiLine.setText(cadena)

        }

        binding.boton.setOnClickListener {
            finish()
        }

    }
}