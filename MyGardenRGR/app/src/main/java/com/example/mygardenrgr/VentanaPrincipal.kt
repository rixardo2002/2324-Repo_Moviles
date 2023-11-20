package com.example.mygardenrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mygardenrgr.databinding.ActivityVentanaPrincipalBinding

class VentanaPrincipal : AppCompatActivity() {

    lateinit var binding:ActivityVentanaPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Agregar un botón de regreso
        binding.btnRegresar.setOnClickListener {
            // Puedes usar onBackPressed() o crear un Intent para volver a la pantalla Home
            finish()
        }



    }//OVERRIDE






}