package com.example.variosactivitypasodatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.variosactivitypasodatos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boton.setOnClickListener{
            irVentana2()
        }
    }


    private fun irVentana2(){
        //podriamos crear aqui el objeto y pasarlo, en vez de los atributos individualmente
        //persona deberia ser serializable en ese caso
        var miIntent: Intent = Intent(this, Ventana2::class.java)
        //miIntent.putExtra("nombre", binding.cajaNombre.txt.toString())
        //miIntent.putExtra("edad", binding.cajaEdad.txt.toString())
        startActivity(miIntent)

    }
}

