package com.example.futbolistasviewrgr

import Modelo.Futbolista
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.futbolistasviewrgr.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
lateinit var binding: ActivityMain2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var p  = intent.getSerializableExtra("obj") as Futbolista
        var nombre=p.nombre
        var apellido=p.apellido
        var equipo=p.equipo
        var contenido=contenidoDescripcion(nombre)
        binding.txtDescripcion.text = contenido
        binding.textName.text=nombre
        binding.textSurname.text=apellido
        eleccionImagen(equipo)
        binding.btnVolver.setOnClickListener {
            finish()
        }

    }

    private fun contenidoDescripcion(nombre:String):String{
        var descripcion:String
        if(nombre.equals("Cristiano")){
        descripcion="Jugador portugues, cuya pierna buena es la derecha, se caracteriza por su increíble salto y velocidad "
        }
        else if (nombre.equals("Fernando")){
            descripcion="Jugador español, diestro, delantero caracterizado por el tiro"
        }
        else if (nombre.equals("Andres")){
            descripcion="Jugador español, mediocentro ambidiestro, caracterizado por su increíble pase, visión de juego y regate"
        }
        else if (nombre.equals("Iker")){
            descripcion="Jugador español, portero, caracterizado por su increíble velocidad y reflejos"
        }
        else if (nombre.equals("Leo")){
            descripcion="Jugador argentino, zurdo, caracterizado por su regate y tiro con definición y un gran lanzador de faltas"
        }
        else{
            descripcion=""
        }
        return descripcion
    }

    private fun eleccionImagen(equipo:String){
        if (equipo.equals("sevilla")){
            binding.imageView.setImageResource(R.drawable.sevilla)
        }
        else if (equipo.equals("betis")){
            binding.imageView.setImageResource(R.drawable.betis)

        }else{
            binding.imageView.setImageResource(R.drawable.madrid)

        }
    }
}