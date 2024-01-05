package com.example.mygardenrgr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VentanaOpcion1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_opcion1)

        val btnAbrirEnlace1: Button = findViewById(R.id.btnAbrirEnlace1)
        val btnAbrirEnlace2: Button = findViewById(R.id.btnAbrirEnlace2)

        //Al pulsar el botón nos llevará a su respectiva función
        btnAbrirEnlace1.setOnClickListener {
            abrirEnlace1()
        }




    }//Override

    private fun abrirEnlace1() {
        val url = "https://github.com/rixardo2002/2324-Repo_Moviles"
        abrirEnlace(url)
    }


    private fun abrirEnlace(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }



}